package com.example.dyeTrack.in.groupeMusculaire;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.service.GroupeMusculaireService;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/MuscleGroup")
public class GroupeMusculaireController {
    private GroupeMusculaireService service;

    public GroupeMusculaireController(GroupeMusculaireService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<MuscleGroup>>> getAll() {
        return ResponseBuilder.success(service.getAll(), "Liste des groupes musculaires récupérée avec succès");
    }
}
