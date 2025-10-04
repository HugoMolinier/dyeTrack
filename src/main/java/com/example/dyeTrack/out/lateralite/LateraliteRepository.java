package com.example.dyeTrack.out.lateralite;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.Lateralite;


public interface LateraliteRepository extends JpaRepository<Lateralite,Long> {
    Lateralite findOneByNomFRAndNomEN(String nomFR, String nomEN);
}
