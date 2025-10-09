package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.port.in.GroupeMusculaireUseCase;
import com.example.dyeTrack.core.port.out.GroupeMusculairePort;

@Service
public class GroupeMusculaireService implements GroupeMusculaireUseCase {

    private final GroupeMusculairePort groupePort;

    public GroupeMusculaireService(GroupeMusculairePort groupePort) {
        this.groupePort = groupePort;
    }

    public List<GroupeMusculaire> getAll() {
        return groupePort.getAll();
    }

}
