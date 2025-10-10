package com.example.dyeTrack.in.equipement;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Equipement;
import com.example.dyeTrack.core.service.EquipementService;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/Equipement")
public class EquipementController {
    private EquipementService service;

    public EquipementController(EquipementService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<Equipement>>> getAll() {
        return ResponseBuilder.success(service.getAll(), "Liste des équipements récupérée avec succès");
    }
}
