package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.port.in.EquipementUseCase;
import com.example.dyeTrack.core.port.out.EquipementPort;


@Service
public class EquipementService implements EquipementUseCase {

    private EquipementPort equipementPort;

    public EquipementService (EquipementPort equipementPort){
        this.equipementPort = equipementPort;
    }


    public List<Equipement> getAll(){
        return equipementPort.getAll();
    }



    
}
