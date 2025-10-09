package com.example.dyeTrack.in.exercise;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.service.ExerciseService;
import com.example.dyeTrack.core.valueobject.IDNameValue;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceLightReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciseCreateDTO;
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
    public List<? extends ExerciceLightReturnDTO> getAll(
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
            List<ExerciceLightReturnDTO> exercicesOut = new ArrayList<>();
            for (Exercise exercice : exercises) {
                exercicesOut.add(new ExerciceLightReturnDTO(exercice));
            }
            return exercicesOut;
        }

        List<ExerciceDetailReturnDTO> result = new ArrayList<>();
        for (Exercise e : exercises) {
            result.add(buildDetailDTO(e, showMuscles, showMainFocusMuscularGroup));
        }
        return result;
    }

    @GetMapping("/getById/{id}")
    public ExerciceReturnDTO getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showMuscles,
            @RequestParam(defaultValue = "false") Boolean showMainFocusMuscularGroup,
            @RequestParam(defaultValue = "false") Boolean onlyPrincipalMuscle) {

        Exercise exercice = exerciseService.getByIdExercise(id, onlyPrincipalMuscle);

        if (!showMuscles) {
            return new ExerciceLightReturnDTO(exercice);
        }

        return buildDetailDTO(exercice, showMuscles, showMainFocusMuscularGroup);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ExerciceDetailReturnDTO create(@RequestBody @Valid ExerciseCreateDTO exercisedto) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise exercice = exerciseService.create(
                exercisedto.getNameFR(),
                exercisedto.getDescription(),
                exercisedto.getLinkVideo(),
                idTokenUser,
                exercisedto.getRelExerciseMuscles());

        return buildDetailDTO(exercice, true, true);
    }

    @Transactional
    @PostMapping("/createMultiple")
    @Operation(summary = "Create Multiple Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public List<ExerciceLightReturnDTO> createMultiple(@RequestBody List<ExerciseCreateDTO> exercises) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<ExerciceLightReturnDTO> exercicesOut = new ArrayList<>();
        for (ExerciseCreateDTO ex : exercises) {
            exercicesOut.add(new ExerciceLightReturnDTO(exerciseService.create(
                    ex.getNameFR(),
                    ex.getDescription(),
                    ex.getLinkVideo(),
                    idTokenUser,
                    ex.getRelExerciseMuscles())));
        }
        return exercicesOut;
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ExerciceDetailReturnDTO update(@PathVariable Long id,
            @RequestBody @Valid ExerciseCreateDTO dto) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        Exercise updatedExercise = exerciseService.update(id, idTokenUser, dto.getNameFR(), dto.getDescription(),
                dto.getLinkVideo(),
                dto.getRelExerciseMuscles());
        return buildDetailDTO(updatedExercise, true, true);

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<String> delete(@PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.delete(id, idTokenUser);
        return ResponseEntity.ok("Exercise deleted successfully");
    }

    // Helper
    private ExerciceDetailReturnDTO buildDetailDTO(Exercise exercise, boolean includeMuscles,
            boolean includeMainGroup) {
        List<MuscleInfo> muscles = new ArrayList<>();
        IDNameValue mainFocusGroup = null;

        for (RelExerciseMuscle rel : exercise.getRelExerciseMuscles()) {
            if (includeMuscles) {
                muscles.add(new MuscleInfo(
                        rel.getMuscle().getId(),
                        rel.isPrincipal(),
                        rel.getMuscle().getNameFR(),
                        rel.getMuscle().getNameEN()));
            }

            if (includeMainGroup && rel.isPrincipal() && mainFocusGroup == null) {
                GroupeMusculaire gm = rel.getMuscle().getGroupeMusculaire();
                if (gm != null) {
                    mainFocusGroup = new IDNameValue(gm.getId(), gm.getNomFR(), gm.getNomEN());
                }
            }
        }

        return new ExerciceDetailReturnDTO(exercise, muscles, mainFocusGroup);
    }

}
