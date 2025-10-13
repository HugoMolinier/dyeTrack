package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUserId;

public interface InfoExerciseUserPort {
    InfoExerciseUser save(InfoExerciseUser relRecensementExercise);

    InfoExerciseUser getById(InfoExerciseUserId id);

    List<InfoExerciseUser> getAll(Boolean favorite, Boolean withNote, Long idUser);

    void delete(InfoExerciseUser relRecensementExercise);
}
