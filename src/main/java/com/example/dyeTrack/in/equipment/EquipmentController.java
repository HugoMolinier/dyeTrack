package com.example.dyeTrack.in.equipment;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Equipment;
import com.example.dyeTrack.core.service.EquipmentService;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/Equipment")
public class EquipmentController {
    private EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<Equipment>>> getAll() {
        return ResponseBuilder.success(service.getAll(), "Liste des équipements récupérée avec succès");
    }
}
