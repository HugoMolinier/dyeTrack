package com.example.dyeTrack.out.muscle;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.port.out.MusclePort;

@Component
public class MuscleAdapter implements MusclePort {
    private MuscleRepository repository;

    public MuscleAdapter(MuscleRepository repository) {
        this.repository = repository;
    }

    @Cacheable("muscles")
    public List<Muscle> getAll() {
        return repository.findAll();
    }

    public Muscle getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Muscle> getByName(String name) {
        return repository.findByName(name);
    }

    public List<Muscle> getByIDGroupeMuscle(List<Integer> groupeMusculaires) {
        return repository.findByGroupeMusculaireIds(groupeMusculaires);
    }

    @Override
    public void save(Muscle muscle) {
        repository.save(muscle);
    }

}
