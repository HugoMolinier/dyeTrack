package com.example.dyeTrack.out.equipement;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.Equipement;


public interface EquipementRepository extends JpaRepository<Equipement,Long> {
    Equipement findOneByNomFRAndNomEN(String nomFR, String nomEN);
}
