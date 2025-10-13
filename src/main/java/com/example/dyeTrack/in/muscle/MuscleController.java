package com.example.dyeTrack.in.muscle;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.service.MuscleService;
import com.example.dyeTrack.in.muscle.dto.ReturnMuscleDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;

@RestController
@RequestMapping("/api/muscle")
public class MuscleController {
   private MuscleService service;

   public MuscleController(MuscleService service) {
      this.service = service;
   }

   @GetMapping("/")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<ReturnMuscleDTO>>> getAll() {
      return ResponseBuilder.success(listToDTO(service.getAll()), "Liste de tous les muscles récupérée avec succès");
   }

   @GetMapping("/{id}")
   public ResponseEntity<ResponseBuilder.ResponseDTO<ReturnMuscleDTO>> get(@PathVariable Long id) {
      return ResponseBuilder.success(new ReturnMuscleDTO(service.getById(id)), "Muscle récupéré avec succès");
   }

   @GetMapping("/getByName/{name}")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<ReturnMuscleDTO>>> get(@PathVariable String name) {
      return ResponseBuilder.success(listToDTO(service.getByName(name)), "Muscles récupérés par name avec succès");
   }

   @GetMapping("/getByGroupe")
   public ResponseEntity<ResponseBuilder.ResponseDTO<List<ReturnMuscleDTO>>> getByMuscleGroup(
         @RequestParam List<Integer> idDGroupeMuscle) {
      return ResponseBuilder.success(listToDTO(service.getByIDGroupeMuscle(idDGroupeMuscle)),
            "Muscles récupérés par groupe musculaire avec succès");
   }

   private List<ReturnMuscleDTO> listToDTO(List<Muscle> muscles) {
      return muscles.stream()
            .map(ReturnMuscleDTO::new) // utilise le constructeur qui prend Muscle
            .collect(Collectors.toList());
   }

}
