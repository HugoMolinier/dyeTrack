package com.example.dyeTrack.out.infoExerciseUser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUserId;
import com.example.dyeTrack.core.port.out.InfoExerciseUserPort;

@Component
public class InfoExerciseUserAdapter implements InfoExerciseUserPort {
    private final InfoExerciseUserRepository infoExerciseUserRepository;

    public InfoExerciseUserAdapter(InfoExerciseUserRepository infoExerciseUserRepository) {
        this.infoExerciseUserRepository = infoExerciseUserRepository;
    }

    @Override
    public InfoExerciseUser save(InfoExerciseUser infoExerciseUser) {
        return infoExerciseUserRepository.save(infoExerciseUser);
    }

    @Override
    public List<InfoExerciseUser> getAll(Long idUser) {
        return infoExerciseUserRepository.getAllWithFilter(idUser);
    }

    @Override
    public InfoExerciseUser getById(InfoExerciseUserId id) {
        return infoExerciseUserRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(InfoExerciseUser infoExerciseUser) {
        infoExerciseUserRepository.delete(infoExerciseUser);
    }

}
