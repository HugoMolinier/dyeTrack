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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
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

        List<ExerciceDetailReturnDTO> exercicesOut = new ArrayList<>();

        for (Exercise exercice : exercises) {
            List<MuscleInfo> muscles = new ArrayList<>();
            IDNameValue mainFocusGroup = null;

            for (RelExerciseMuscle rem : exercice.getRelExerciseMuscles()) {
                if (showMuscles) {
                    muscles.add(new MuscleInfo(rem.getMuscle().getId(), rem.isPrincipal(), rem.getMuscle().getNameFR(),
                            rem.getMuscle().getNameEN()));
                }
                if (showMainFocusMuscularGroup && rem.isPrincipal() && mainFocusGroup == null) {
                    GroupeMusculaire gm = rem.getMuscle().getGroupeMusculaire();
                    if (gm != null) {
                        mainFocusGroup = new IDNameValue(gm.getId(), gm.getNomFR(), gm.getNomEN());
                    }
                }
            }
            exercicesOut.add(new ExerciceDetailReturnDTO(exercice, muscles, mainFocusGroup));
        }

        return exercicesOut;
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

        List<MuscleInfo> muscles = exercice.getRelExerciseMuscles().stream()
                .map(rem -> new MuscleInfo(rem.getMuscle().getId(), rem.isPrincipal(), rem.getMuscle().getNameFR(),
                        rem.getMuscle().getNameEN()))
                .toList();

        return new ExerciceDetailReturnDTO(exercice, muscles, null);
    }

    @PostMapping("/create")
    @Operation(summary = "Create Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> create(@RequestBody @Valid ExerciseCreateDTO exercisedto,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.create(exercisedto.getNameFR(), exercisedto.getDescription(), exercisedto.getLinkVideo(),
                idTokenUser, exercisedto.getRelExerciseMuscles());
        return ResponseEntity.status(HttpStatus.CREATED).body("Exercise created successfully");
    }

    @Transactional
    @PostMapping("/createMultiple")
    @Operation(summary = "Create Multiple Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<String> createMultiple(@RequestBody List<ExerciseCreateDTO> exercises,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        for (ExerciseCreateDTO ex : exercises) {
            exerciseService.create(
                    ex.getNameFR(),
                    ex.getDescription(),
                    ex.getLinkVideo(),
                    idTokenUser,
                    ex.getRelExerciseMuscles());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Exercises created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> update(@PathVariable Long id,
            HttpServletRequest request,
            @RequestBody @Valid ExerciseCreateDTO dto) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.update(id, idTokenUser, dto.getNameFR(), dto.getDescription(), dto.getLinkVideo(),
                dto.getRelExerciseMuscles());
        return ResponseEntity.ok("Exercise updated successfully");

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete Exercice information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<String> delete(@PathVariable Long id,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        exerciseService.delete(id, idTokenUser);
        return ResponseEntity.ok("Exercise deleted successfully");
    }

}
