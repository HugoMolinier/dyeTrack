package com.example.dyeTrack.out.infoExerciceUser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUserId;
import com.example.dyeTrack.core.port.out.InfoExerciceUserPort;

@Component
public class InfoExerciceUserAdapter implements InfoExerciceUserPort {
    private final InfoExerciceUserRepository infoExerciceUserRepository;

    public InfoExerciceUserAdapter(InfoExerciceUserRepository infoExerciceUserRepository) {
        this.infoExerciceUserRepository = infoExerciceUserRepository;
    }

    @Override
    public InfoExerciceUser save(InfoExerciceUser infoExerciceUser) {
        return infoExerciceUserRepository.save(infoExerciceUser);
    }

    @Override
    public List<InfoExerciceUser> getAll(Boolean favorie, Boolean withNote, Long idUser) {
        return infoExerciceUserRepository.getAllWithFilter(favorie, withNote, idUser);
    }

    @Override
    public InfoExerciceUser getById(InfoExerciceUserId id) {
        return infoExerciceUserRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(InfoExerciceUser infoExerciceUser) {
        infoExerciceUserRepository.delete(infoExerciceUser);
    }

}
