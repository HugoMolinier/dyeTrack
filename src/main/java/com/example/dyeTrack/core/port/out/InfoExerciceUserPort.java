package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUserId;

public interface InfoExerciceUserPort {
    InfoExerciceUser save(InfoExerciceUser relRecensementExercice);

    InfoExerciceUser getById(InfoExerciceUserId id);

    List<InfoExerciceUser> getAll(Boolean favorie, Boolean withNote, Long idUser);

    void delete(InfoExerciceUser relRecensementExercice);
}
