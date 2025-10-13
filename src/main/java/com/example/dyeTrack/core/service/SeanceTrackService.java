package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.PlannedExercise;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.SeanceTrackUseCase;
import com.example.dyeTrack.core.port.out.DayDataOfUserPort;
import com.example.dyeTrack.core.port.out.EquipmentPort;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.PresetSeancePort;
import com.example.dyeTrack.core.port.out.SeanceTrackPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.PlannedExerciseVO;
import com.example.dyeTrack.core.valueobject.SeanceTrackVO;
import com.example.dyeTrack.core.valueobject.SetOfPlannedExerciseVO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class SeanceTrackService implements SeanceTrackUseCase {

    private final SeanceTrackPort seanceTrackPort;
    private final DayDataOfUserPort dataOfUserPort;
    private final PresetSeancePort presetSeancePort;
    private final LateralitePort lateralitePort;
    private final EquipmentPort equipmentPort;
    private final ExercisePort exercisePort;

    public SeanceTrackService(SeanceTrackPort seanceTrackPort, DayDataOfUserPort dataOfUserPort,
            ExercisePort exercisePort, LateralitePort lateralitePort, EquipmentPort equipmentPort,
            PresetSeancePort presetSeancePort) {
        this.seanceTrackPort = seanceTrackPort;
        this.dataOfUserPort = dataOfUserPort;
        this.exercisePort = exercisePort;
        this.equipmentPort = equipmentPort;
        this.lateralitePort = lateralitePort;
        this.presetSeancePort = presetSeancePort;
    }

    @Transactional
    public SeanceTrack createOrUpdateSeanceTrack(DayDataOfUser dataOfUser, Long userId, SeanceTrackVO vo) {

        if (dataOfUser == null)
            throw new EntityNotFoundException("DayData not found");
        if (!dataOfUser.getUser().getId().equals(userId))
            throw new ForbiddenException("Not dataOfUser of the user");

        SeanceTrack seanceTrack = dataOfUser.getSeanceTrack();
        if (seanceTrack == null) {
            seanceTrack = new SeanceTrack();
            seanceTrack.setDataOfUser(dataOfUser);
        }
        List<Equipment> equipments = equipmentPort.getAll();
        List<Lateralite> lateralites = lateralitePort.getAll();

        seanceTrack.setStartHour(vo.getStartHour());

        // Charger preset si existant
        if (vo.getPresetSeanceId() != null) {
            PresetSeance preset = presetSeancePort.getById(vo.getPresetSeanceId());
            if (preset == null)
                throw new EntityNotFoundException("Preset not found with id " + vo.getPresetSeanceId());
            seanceTrack.setPresetSeance(preset);
        }

        if (seanceTrack.getPlannedExercises() == null) {
            seanceTrack.setPlannedExercises(new ArrayList<>());
        }
        seanceTrack.getPlannedExercises().clear();
        // 🧩 2. Ajouter les nouveaux exercices à la séance
        for (PlannedExerciseVO exerciseVO : vo.getPlannedExerciseVOs()) {
            PlannedExercise plannedExercise = new PlannedExercise();
            plannedExercise.setExerciseOrder(exerciseVO.getExerciseOrder());
            plannedExercise.setExercise(EntityUtils.getExerciseOrThrow(exerciseVO.getExerciseId(), exercisePort));
            Lateralite lateralite = lateralites.stream()
                    .filter(l -> l.getId().equals(exerciseVO.getLateraliteId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Lateralite ID " + exerciseVO.getLateraliteId() + " invalide"));

            Equipment equipment = equipments.stream()
                    .filter(eq -> eq.getId().equals(exerciseVO.getEquipmentId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Equipment ID " + exerciseVO.getEquipmentId() + " invalide"));

            plannedExercise.setLateralite(lateralite);
            plannedExercise.setEquipment(equipment);
            plannedExercise.setSeanceTrack(seanceTrack);

            // 🔁 Ajouter les sets liés à cet exercice
            for (SetOfPlannedExerciseVO setVO : exerciseVO.getSets()) {
                SetOfPlannedExercise set = new SetOfPlannedExercise();
                set.setSetOrder(setVO.getSetOrder());
                set.setRepsNumber(setVO.getRepsNumber());
                set.setRir(setVO.getRir());
                set.setCharge(setVO.getCharge());
                set.setTypeOfSet(setVO.getTypeOfSet());
                set.setPlannedExercise(plannedExercise);

                plannedExercise.getSetsOfPlannedExercise().add(set);
            }

            seanceTrack.getPlannedExercises().add(plannedExercise);
        }
        return seanceTrackPort.save(seanceTrack);
    }

    @Transactional
    public SeanceTrack getSeanceOfDay(Long dayDataOfUserId, Long userId) {
        if (dayDataOfUserId == null)
            throw new IllegalArgumentException("dayDataOfUserId empty");

        DayDataOfUser dataOfUser = dataOfUserPort.getById(dayDataOfUserId);
        if (dataOfUser == null)
            throw new EntityNotFoundException("DayData not found");

        if (!dataOfUser.getUser().getId().equals(userId))
            throw new ForbiddenException("Not dataOfUser of the user");

        SeanceTrack seanceTrack = dataOfUser.getSeanceTrack();
        if (seanceTrack == null)
            throw new EntityNotFoundException("No SeanceTrack found for this day");

        return seanceTrack;
    }

    @Transactional
    public List<SeanceTrack> getAllSeancesOfUser(Long userId) {
        if (userId == null)
            throw new IllegalArgumentException("userId empty");
        List<DayDataOfUser> allDayData = dataOfUserPort.getAll(userId);
        if (allDayData.isEmpty())
            throw new EntityNotFoundException("No DayData found for this user");
        return allDayData.stream()
                .map(DayDataOfUser::getSeanceTrack)
                .filter(seance -> seance != null)
                .toList();
    }

}
