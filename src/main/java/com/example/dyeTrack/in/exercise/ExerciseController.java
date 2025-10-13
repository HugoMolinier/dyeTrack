package com.example.dyeTrack.in.exercise;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.MuscleGroup;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciseDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/Exercise")
public class ExerciseController {
    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ExerciseDetailReturnDTO>>> getAll(
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") Boolean officialExercise,
            @RequestParam(required = false) Long idUser,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle,
            @RequestParam(defaultValue = "false") Boolean showMainFocusMuscularGroup,
            @RequestParam(required = false) List<Integer> idsGroupeMuscle,
            @RequestParam(required = false) List<Integer> idsMuscle,
            @RequestParam(required = false) List<Long> idsExercise) {

        List<Exercise> exercises = exerciseService.getAll(name, officialExercise, idUser, onlyPrincipalMuscle,
                showMainFocusMuscularGroup, idsGroupeMuscle, idsMuscle, idsExercise);

        if (!showMuscles && !showMainFocusMuscularGroup) {
            List<ExerciseDetailReturnDTO> exercisesOut = new ArrayList<>();
            for (Exercise exercise : exercises) {
                exercisesOut.add(new ExerciseDetailReturnDTO(exercise));
            }
            return ResponseBuilder.success(exercisesOut, "Liste des exercises récupérée avec succès");
        }

        List<ExerciseDetailReturnDTO> result = new ArrayList<>();
        for (Exercise e : exercises) {
            result.add(buildDetailDTO(e, showMuscles, showMainFocusMuscularGroup));
        }
        return ResponseBuilder.success(result, "Liste des exercises récupérée avec succès");

    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(defaultValue = "false") Boolean showMainFocusMuscularGroup,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle) {

        Exercise exercise = exerciseService.getByIdExercise(id, onlyPrincipalMuscle);

        if (!showMuscles) {
            return ResponseBuilder.success(new ExerciseDetailReturnDTO(exercise),
                    "Exercise récupérée avec succès");
        }

        return ResponseBuilder.success(buildDetailDTO(exercise, showMuscles, showMainFocusMuscularGroup),
                "Exercise récupérée avec succès");
    }

    @PostMapping("/create")
    @Operation(summary = "Create Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> create(
            @RequestBody @Valid ExerciseCreateDTO exercisedto) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise exercise = exerciseService.create(
                exercisedto.getNameFR(),
                exercisedto.getDescription(),
                exercisedto.getLinkVideo(),
                idTokenUser,
                exercisedto.getRelExerciseMuscles());

        return ResponseBuilder.created(buildDetailDTO(exercise, true, true), "Exercise créé avec succès");
    }

    @Transactional
    @PostMapping("/createMultiple")
    @Operation(summary = "Create Multiple Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ExerciseDetailReturnDTO>>> createMultiple(
            @RequestBody List<ExerciseCreateDTO> exercises) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<ExerciseDetailReturnDTO> exercisesOut = new ArrayList<>();
        for (ExerciseCreateDTO ex : exercises) {
            exercisesOut.add(new ExerciseDetailReturnDTO(exerciseService.create(
                    ex.getNameFR(),
                    ex.getDescription(),
                    ex.getLinkVideo(),
                    idTokenUser,
                    ex.getRelExerciseMuscles())));
        }
        return ResponseBuilder.created(exercisesOut, "Exercises créés avec succès");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<ExerciseDetailReturnDTO>> update(@PathVariable Long id,
            @RequestBody @Valid ExerciseCreateDTO dto) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise updatedExercise = exerciseService.update(id, idTokenUser, dto.getNameFR(), dto.getDescription(),
                dto.getLinkVideo(),
                dto.getRelExerciseMuscles());
        return ResponseBuilder.success(buildDetailDTO(updatedExercise, true, true), "Exercise mis à jour avec succès");

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Exercise information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> delete(@PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.delete(id, idTokenUser);
        return ResponseBuilder.success("Exercise deleted successfully", "Suppression réussie");

    }

    // Helper
    private ExerciseDetailReturnDTO buildDetailDTO(Exercise exercise, boolean includeMuscles,
            boolean includeMainGroup) {
        List<MuscleInfo> muscles = new ArrayList<>();
        Long mainFocusGroup = null;

        for (RelExerciseMuscle rel : exercise.getRelExerciseMuscles()) {
            if (includeMuscles) {
                muscles.add(new MuscleInfo(
                        rel.getMuscle().getId(),
                        rel.isPrincipal()));
            }

            if (includeMainGroup && rel.isPrincipal() && mainFocusGroup == null) {
                MuscleGroup gm = rel.getMuscle().getGroupeMusculaire();
                if (gm != null) {
                    mainFocusGroup = gm.getId();
                }
            }
        }

        return new ExerciseDetailReturnDTO(exercise, muscles, mainFocusGroup);
    }

}
