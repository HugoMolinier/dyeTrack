package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.PresetSeanceUseCase;
import com.example.dyeTrack.core.port.out.EquipementPort;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.PresetSeanceExercicePort;
import com.example.dyeTrack.core.port.out.PresetSeancePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;

import jakarta.transaction.Transactional;

@Service
public class PresetSeanceService implements PresetSeanceUseCase {

    private PresetSeancePort presetSeancePort;
    private UserPort userPort;
    private LateralitePort lateralitePort;
    private EquipementPort equipementPort;
    private ExercisePort exercisePort;
    private PresetSeanceExercicePort presetSeanceExercicePort;

    public PresetSeanceService(PresetSeancePort presetSeancePort, UserPort userPort, LateralitePort lateralitePort,
            EquipementPort equipementPort, ExercisePort exercisePort,
            PresetSeanceExercicePort presetSeanceExercicePort) {
        this.presetSeancePort = presetSeancePort;
        this.userPort = userPort;
        this.equipementPort = equipementPort;
        this.lateralitePort = lateralitePort;
        this.exercisePort = exercisePort;
        this.presetSeanceExercicePort = presetSeanceExercicePort;
    }

    @Transactional
    public PresetSeance save(String name, Long idUserWhoAdd, List<PresetSeanceExerciceVO> presetSeanceExercice) {
        if (name == null)
            throw new ExerciseCreationException("name cannot be empty");
        User user = userPort.get(idUserWhoAdd);
        if (user == null)
            throw new ExerciseCreationException("user Not found with id " + idUserWhoAdd);
        PresetSeance presetSeance = presetSeancePort.save(new PresetSeance(name, user));

        // do relExerciceMuscle
        List<PresetSeanceExercice> listExerciceToAdd = new ArrayList<PresetSeanceExercice>();
        if (presetSeanceExercice != null && !presetSeanceExercice.isEmpty()) {

            int index = 1;
            List<Equipement> equipements = equipementPort.getAll();
            List<Lateralite> lateralites = lateralitePort.getAll();
            for (PresetSeanceExerciceVO presetSeanceExerciceVO : presetSeanceExercice) {
                Long idExercice = presetSeanceExerciceVO.getIdExercice();
                Long idLateralite = presetSeanceExerciceVO.getIdLateralite();
                Long idEquipement = presetSeanceExerciceVO.getIdEquipement();

                Exercise exercice = exercisePort.getByIdExercise(idExercice);
                if (exercice == null) {
                    throw new IllegalArgumentException("Exercice ID " + idExercice + " invalide");
                }

                Lateralite lateralite = lateralites.stream()
                        .filter(l -> l.getId().equals(idLateralite))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Lateralite ID " + idLateralite + " invalide"));

                Equipement equipement = equipements.stream()
                        .filter(eq -> eq.getId().equals(idEquipement))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Equipement ID " + idEquipement + " invalide"));

                listExerciceToAdd.add(new PresetSeanceExercice(
                        presetSeance,
                        exercice,
                        presetSeanceExerciceVO.getParameter(),
                        presetSeanceExerciceVO.getRangeRepInf(),
                        presetSeanceExerciceVO.getRangeRepSup(),
                        index,
                        lateralite,
                        equipement));
                index++;
            }
            presetSeanceExercicePort.saveAll(listExerciceToAdd);
        }
        ;
        return presetSeance;

    }

    public List<PresetSeance> getAllPresetOfUser(Long idUser, String name) {
        return presetSeancePort.findAllPresetOfUser(idUser, name);
    }

    public PresetSeance getById(Long idPreset, Long idUser) {
        PresetSeance presetSeance = presetSeancePort.getById(idPreset);
        if (!presetSeance.getUser().getId().equals(idUser)) {
            throw new ForbiddenException("Accès interdit");
        }
        return presetSeance;
    }

    public PresetSeance update(Long idPreset, Long idUserQuiModifie, String newName,
            List<PresetSeanceExerciceVO> presetSeanceExercice) {
        if (idPreset == null)
            throw new ForbiddenException("idPreset empty");
        PresetSeance presetSeance = presetSeancePort.getById(idPreset);
        if (presetSeance == null)
            throw new ForbiddenException("Preset Not found with id " + idPreset);
        if (!presetSeance.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas modifier ce preset");

        if (newName != null) {
            presetSeance.setName(newName);
            presetSeancePort.update(presetSeance);
        }

        // gestion relExerciceMuscles a faire
        List<PresetSeanceExercice> listExerciceToAdd = new ArrayList<PresetSeanceExercice>();
        if (presetSeanceExercice != null && !presetSeanceExercice.isEmpty()) {
            presetSeanceExercicePort.deleteByPresetId(idPreset);
            int index = 1;
            List<Equipement> equipements = equipementPort.getAll();
            List<Lateralite> lateralites = lateralitePort.getAll();
            for (PresetSeanceExerciceVO presetSeanceExerciceVO : presetSeanceExercice) {
                Long idExercice = presetSeanceExerciceVO.getIdExercice();
                Long idLateralite = presetSeanceExerciceVO.getIdLateralite();
                Long idEquipement = presetSeanceExerciceVO.getIdEquipement();

                Exercise exercice = exercisePort.getByIdExercise(idExercice);
                if (exercice == null) {
                    throw new IllegalArgumentException("Exercice ID " + idExercice + " invalide");
                }

                Lateralite lateralite = lateralites.stream()
                        .filter(l -> l.getId().equals(idLateralite))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Lateralite ID " + idLateralite + " invalide"));

                Equipement equipement = equipements.stream()
                        .filter(eq -> eq.getId().equals(idEquipement))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Equipement ID " + idEquipement + " invalide"));

                listExerciceToAdd.add(new PresetSeanceExercice(
                        presetSeance,
                        exercice,
                        presetSeanceExerciceVO.getParameter(),
                        presetSeanceExerciceVO.getRangeRepInf(),
                        presetSeanceExerciceVO.getRangeRepSup(),
                        index,
                        lateralite,
                        equipement));
                index++;
            }
            presetSeanceExercicePort.saveAll(listExerciceToAdd);
        }

        return presetSeance;
    }

    public void delete(Long idpresetSeance, Long idUserQuiModifie) {
        if (idpresetSeance == null)
            throw new ForbiddenException("idPreset empty");
        PresetSeance presetSeance = presetSeancePort.getById(idpresetSeance);
        if (presetSeance == null)
            throw new ForbiddenException("Preset Not found with id " + idpresetSeance);
        if (!presetSeance.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas delete ce preset");

        presetSeancePort.delete(presetSeance);
    }

}
