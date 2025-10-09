package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;

public interface InfoExerciceUserUseCase {
    InfoExerciceUser update(Long exerciceId, Long userId, Boolean favorie, String note);

    List<InfoExerciceUser> getAll(Boolean favorie, Boolean withNote, Long idUser);

}
