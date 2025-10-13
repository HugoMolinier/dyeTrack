package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.port.in.EquipmentUseCase;
import com.example.dyeTrack.core.port.out.EquipmentPort;

@Service
public class EquipmentService implements EquipmentUseCase {

    private final EquipmentPort equipmentPort;

    public EquipmentService(EquipmentPort equipmentPort) {
        this.equipmentPort = equipmentPort;
    }

    public List<Equipment> getAll() {
        return equipmentPort.getAll();
    }

}
