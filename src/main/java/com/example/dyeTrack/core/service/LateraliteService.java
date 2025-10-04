package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.port.in.LateraliteUseCase;
import com.example.dyeTrack.core.port.out.LateralitePort;


@Service
public class LateraliteService implements LateraliteUseCase {

    private LateralitePort lateralitePort;

    public LateraliteService (LateralitePort lateralitePort){
        this.lateralitePort = lateralitePort;
    }


    public List<Lateralite> getAll(){
        return lateralitePort.getAll();
    }



    
}
