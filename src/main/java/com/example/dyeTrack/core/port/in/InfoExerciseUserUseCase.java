package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;

public interface InfoExerciseUserUseCase {
    InfoExerciseUser update(Long exerciseId, Long userId, Boolean favorite, String note);

    List<InfoExerciseUser> getAll(Long idUser);

}
