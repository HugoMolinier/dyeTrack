package com.example.dyeTrack.out.groupeMusculaire;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.port.out.GroupeMusculairePort;

@Component
public class GroupeMusculaireAdapter implements GroupeMusculairePort {
    private GroupeMusculaireRepository repository;

    public GroupeMusculaireAdapter(GroupeMusculaireRepository repository) {
        this.repository = repository;
    }

    public List<MuscleGroup> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(MuscleGroup groupeMusculaire) {
        repository.save(groupeMusculaire);
    }

}
