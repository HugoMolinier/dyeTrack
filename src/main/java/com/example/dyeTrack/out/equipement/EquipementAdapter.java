package com.example.dyeTrack.out.equipement;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.port.out.EquipementPort;

@Component
public class EquipementAdapter implements EquipementPort {
    private EquipementRepository repository;

    public EquipementAdapter(EquipementRepository repository) {
        this.repository = repository;
    }

    public List<Equipement> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(Equipement equipement) {
        repository.save(equipement);
    }

}
