package com.example.dyeTrack.out.groupeMusculaire;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.GroupeMusculaire;


public interface GroupeMusculaireRepository extends JpaRepository<GroupeMusculaire,Long> {
    GroupeMusculaire findOneByNomFRAndNomEN(String nomFR, String nomEN);
}
