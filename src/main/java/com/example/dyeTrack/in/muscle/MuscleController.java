package com.example.dyeTrack.in.muscle;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.service.MuscleService;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/muscle")
public class MuscleController {
   private MuscleService service;

   public MuscleController(MuscleService service) {
      this.service = service;
   }

   @GetMapping("/")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<Muscle>>> getAll() {
      return ResponseBuilder.success(service.getAll(), "Liste de tous les muscles récupérée avec succès");
   }

   @GetMapping("/{id}")
   public ResponseEntity<ResponseBuilder.ResponseDTO<Muscle>> get(@PathVariable Long id) {
      return ResponseBuilder.success(service.getById(id), "Muscle récupéré avec succès");
   }

   @GetMapping("/getByName/{name}")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<Muscle>>> get(@PathVariable String name) {
      return ResponseBuilder.success(service.getByName(name), "Muscles récupérés par nom avec succès");
   }

   @GetMapping("/getByGroupe")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<Muscle>>> getByGroupeMusculaire(
         @RequestParam List<Integer> idDGroupeMuscle) {
      return ResponseBuilder.success(service.getByIDGroupeMuscle(idDGroupeMuscle),
            "Muscles récupérés par groupe musculaire avec succès");
   }

}
