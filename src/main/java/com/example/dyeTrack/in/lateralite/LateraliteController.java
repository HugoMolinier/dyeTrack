package com.example.dyeTrack.in.lateralite;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.service.LateraliteService;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/Lateralite")
public class LateraliteController {
    private LateraliteService service;

    public LateraliteController(LateraliteService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<Lateralite>>> getAll() {
        return ResponseBuilder.success(service.getAll(), "Liste des latéralités récupérée avec succès");
    }
}
