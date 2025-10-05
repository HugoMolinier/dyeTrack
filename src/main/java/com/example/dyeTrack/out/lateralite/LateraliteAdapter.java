package com.example.dyeTrack.out.lateralite;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.port.out.LateralitePort;

@Component
public class LateraliteAdapter implements LateralitePort {
    private LateraliteRepository repository;

    public LateraliteAdapter(LateraliteRepository repository) {
        this.repository = repository;
    }

    public List<Lateralite> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Lateralite lateralite) {
        repository.save(lateralite);
    }

}
