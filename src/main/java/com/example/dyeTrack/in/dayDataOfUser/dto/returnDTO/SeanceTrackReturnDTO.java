package com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.dyeTrack.core.entity.SeanceTrack;

public class SeanceTrackReturnDTO {

    private LocalTime startHour;

    private Long presetSeanceId;

    private List<PlannedExerciseReturnDTO> plannedExercises;

    public SeanceTrackReturnDTO() {
    }

    public SeanceTrackReturnDTO(SeanceTrack seanceTrack) {
        this.startHour = seanceTrack.getStartHour();
        if (seanceTrack.getPresetSeance() != null) {
            this.presetSeanceId = seanceTrack.getPresetSeance().getIdPresetSeance();
        } else {
            this.presetSeanceId = null; // ou 0L si tu préfères
        }
        this.plannedExercises = seanceTrack.getPlannedExercises().stream().map(PlannedExerciseReturnDTO::new)
                .collect(Collectors.toList());
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public Long getPresetSeanceId() {
        return presetSeanceId;
    }

    public void setPresetSeanceId(Long presetSeanceId) {
        this.presetSeanceId = presetSeanceId;
    }

    public List<PlannedExerciseReturnDTO> getPlannedExercises() {
        return plannedExercises;
    }

    public void setPlannedExercises(List<PlannedExerciseReturnDTO> plannedExercises) {
        this.plannedExercises = plannedExercises;
    }

}
