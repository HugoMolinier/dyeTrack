package com.example.dyeTrack.out.muscleGroup;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.MuscleGroup;

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Long> {
    MuscleGroup findOneByNameFRAndNameEN(String nameFR, String nameEN);
}
