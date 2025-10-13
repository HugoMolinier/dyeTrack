package com.example.dyeTrack.out.equipment;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    Equipment findOneByNameFRAndNameEN(String nameFR, String nameEN);
}
