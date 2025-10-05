package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.GroupeMusculaire;

public interface GroupeMusculairePort {
    List<GroupeMusculaire> getAll();

    void save(GroupeMusculaire nomGroupeMusculaire);

}
