package com.example.dyeTrack.out.groupeMusculaire;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.MuscleGroup;

public interface GroupeMusculaireRepository extends JpaRepository<MuscleGroup, Long> {
    MuscleGroup findOneByNameFRAndNameEN(String nameFR, String nameEN);
}
