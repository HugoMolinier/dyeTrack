package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.PresetSeanceExercise.PresetSeanceExercise;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.PresetSeanceUseCase;
import com.example.dyeTrack.core.port.out.EquipmentPort;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.PresetSeancePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciseVO;

import jakarta.transaction.Transactional;

@Service
public class PresetSeanceService implements PresetSeanceUseCase {

    private final PresetSeancePort presetSeancePort;
    private final UserPort userPort;
    private final LateralitePort lateralitePort;
    private final EquipmentPort equipmentPort;
    private final ExercisePort exercisePort;

    public PresetSeanceService(PresetSeancePort presetSeancePort, UserPort userPort, LateralitePort lateralitePort,
            EquipmentPort equipmentPort, ExercisePort exercisePort) {
        this.presetSeancePort = presetSeancePort;
        this.userPort = userPort;
        this.equipmentPort = equipmentPort;
        this.lateralitePort = lateralitePort;
        this.exercisePort = exercisePort;
    }

    @Transactional
    public PresetSeance save(String name, Long idUserWhoAdd, List<PresetSeanceExerciseVO> presetSeanceExercise) {
        if (name == null)
            throw new IllegalArgumentException("name cannot be empty");
        User user = EntityUtils.getUserOrThrow(idUserWhoAdd, userPort);
        PresetSeance presetSeance = new PresetSeance(name, user);

        // do relExerciseMuscle
        if (presetSeanceExercise != null && !presetSeanceExercise.isEmpty()) {

            int index = 1;
            List<Equipment> equipments = equipmentPort.getAll();
            List<Lateralite> lateralites = lateralitePort.getAll();
            List<PresetSeanceExercise> listExerciseToAdd = new ArrayList<PresetSeanceExercise>();
            for (PresetSeanceExerciseVO presetSeanceExerciseVO : presetSeanceExercise) {
                Long idExercise = presetSeanceExerciseVO.getIdExercise();
                Long idLateralite = presetSeanceExerciseVO.getIdLateralite();
                Long idEquipment = presetSeanceExerciseVO.getIdEquipment();

                Exercise exercise = EntityUtils.getExerciseOrThrow(idExercise, exercisePort);

                Lateralite lateralite = lateralites.stream()
                        .filter(l -> l.getId().equals(idLateralite))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Lateralite ID " + idLateralite + " invalide"));

                Equipment equipment = equipments.stream()
                        .filter(eq -> eq.getId().equals(idEquipment))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Equipment ID " + idEquipment + " invalide"));

                listExerciseToAdd.add(new PresetSeanceExercise(
                        presetSeance,
                        exercise,
                        presetSeanceExerciseVO.getParameter(),
                        presetSeanceExerciseVO.getRangeRepInf(),
                        presetSeanceExerciseVO.getRangeRepSup(),
                        index,
                        lateralite,
                        equipment));
                index++;
            }
            presetSeance.getPresetSeanceExercise().addAll(listExerciseToAdd);
            presetSeancePort.save(presetSeance);
        }
        ;
        return presetSeance;

    }

    public List<PresetSeance> getAllPresetOfUser(Long idUser, String name) {
        return presetSeancePort.findAllPresetOfUser(idUser, name);
    }

    public PresetSeance getById(Long idPreset, Long idUser) {
        PresetSeance presetSeance = presetSeancePort.getById(idPreset);
        if (presetSeance == null)
            throw new EntityNotFoundException("Preset not found with id " + idPreset);
        if (!presetSeance.getUser().getId().equals(idUser))
            throw new ForbiddenException("Accès interdit");
        return presetSeance;
    }

    @Transactional
    public PresetSeance update(Long idPreset, Long idUserQuiModifie, String newName,
            List<PresetSeanceExerciseVO> presetSeanceExerciseVOs) {

        if (idPreset == null)
            throw new IllegalArgumentException("idPreset empty");

        PresetSeance presetSeance = presetSeancePort.getById(idPreset);
        if (presetSeance == null)
            throw new EntityNotFoundException("Preset Not found with id " + idPreset);
        if (!presetSeance.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas modifier ce preset");

        if (newName != null) {
            presetSeance.setName(newName);
        }

        // Mise à jour des exercises associés si fournis
        if (presetSeanceExerciseVOs != null && !presetSeanceExerciseVOs.isEmpty()) {

            List<Equipment> equipments = equipmentPort.getAll();
            List<Lateralite> lateralites = lateralitePort.getAll();

            List<PresetSeanceExercise> listExerciseToAdd = new ArrayList<>();
            int index = 1;

            for (PresetSeanceExerciseVO vo : presetSeanceExerciseVOs) {
                Exercise exercise = EntityUtils.getExerciseOrThrow(vo.getIdExercise(), exercisePort);

                Lateralite lateralite = lateralites.stream()
                        .filter(l -> l.getId().equals(vo.getIdLateralite()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Lateralite ID " + vo.getIdLateralite() + " invalide"));

                Equipment equipment = equipments.stream()
                        .filter(eq -> eq.getId().equals(vo.getIdEquipment()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Equipment ID " + vo.getIdEquipment() + " invalide"));

                listExerciseToAdd.add(new PresetSeanceExercise(
                        presetSeance,
                        exercise,
                        vo.getParameter(),
                        vo.getRangeRepInf(),
                        vo.getRangeRepSup(),
                        index,
                        lateralite,
                        equipment));

                index++;
            }

            presetSeance.getPresetSeanceExercise().clear();
            presetSeance.getPresetSeanceExercise().addAll(listExerciseToAdd);
        }

        return presetSeancePort.update(presetSeance);
    }

    public void delete(Long idpresetSeance, Long idUserQuiModifie) {
        if (idpresetSeance == null)
            throw new IllegalArgumentException("idPreset cannot be null");

        PresetSeance presetSeance = presetSeancePort.getById(idpresetSeance);
        if (presetSeance == null)
            throw new EntityNotFoundException("Preset not found with id " + idpresetSeance);

        if (!presetSeance.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas delete ce preset");

        if (presetSeance.getPresetSeanceExercise() != null && !presetSeance.getPresetSeanceExercise().isEmpty()) {
            presetSeance.getPresetSeanceExercise().clear();
        }
        presetSeancePort.delete(presetSeance);
    }

}
