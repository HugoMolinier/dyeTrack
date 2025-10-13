package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.Equipment;

public interface EquipmentUseCase {
    List<Equipment> getAll();
}