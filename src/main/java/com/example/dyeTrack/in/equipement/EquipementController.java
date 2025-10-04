package com.example.dyeTrack.in.equipement;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.service.EquipementService;

@RestController
@RequestMapping("/api/Equipement")
public class EquipementController {
    private EquipementService service;

    public EquipementController(EquipementService service){
        this.service = service;
    }

    @GetMapping("/")
    public List<Equipement> getAll() {
       return service.getAll();
    }
}
