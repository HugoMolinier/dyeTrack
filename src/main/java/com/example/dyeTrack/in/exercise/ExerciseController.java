package com.example.dyeTrack.in.exercise;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceLightReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/Exercise")
public class ExerciseController {
    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService){
        this.exerciseService = exerciseService;
    }

    @GetMapping("/getAll")
    public List<? extends ExerciceLightReturnDTO> getAll(
        @RequestParam(defaultValue = "false") boolean showMuscles,
        @RequestParam(required = false) String name,
        @RequestParam(defaultValue = "false") Boolean officialExercise,
        @RequestParam(required = false) Long idUser,
        @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle) {

          List<Exercise> exercises = exerciseService.getAll(name, officialExercise, idUser, onlyPrincipalMuscle);

    if (!showMuscles) {
        List<ExerciceLightReturnDTO> exercicesOut = new ArrayList<>();
        for (Exercise exercice : exercises) {
            exercicesOut.add(new ExerciceLightReturnDTO(exercice));
        }
        return exercicesOut;
    }

    List<ExerciceDetailReturnDTO> exercicesOut = new ArrayList<>();
    for (Exercise exercice : exercises) {
        List<MuscleInfo> muscles = exercice.getRelExerciseMuscles().stream()
                .map(rem -> new MuscleInfo(rem.getMuscle().getId(), rem.isPrincipal()))
                .toList();
        exercicesOut.add(new ExerciceDetailReturnDTO(exercice, muscles));
    }
    return exercicesOut;
    }
    
    @GetMapping("/getById/{id}")
    public  ExerciceReturnDTO getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle) {

        Exercise exercice = exerciseService.getByIdExercise(id, onlyPrincipalMuscle);

        if (!showMuscles) {
            return new ExerciceLightReturnDTO(exercice);
        }

        List<MuscleInfo> muscles = exercice.getRelExerciseMuscles().stream()
                .map(rem -> new MuscleInfo(rem.getMuscle().getId(), rem.isPrincipal()))
                .toList();

        return new ExerciceDetailReturnDTO(exercice, muscles);
    }



    
    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid ExerciseCreateDTO exercisedto) {
    try {
        //start transaction 
        exerciseService.create(exercisedto.getNameFR(), exercisedto.getDescription(), exercisedto.getLinkVideo(), exercisedto.getIdUser(),exercisedto.getRelExerciseMuscles());        
        return ResponseEntity.status(HttpStatus.CREATED).body("Exercise created successfully");
    } catch (ExerciseCreationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
}
