package com.example.dyeTrack.in.presetSeance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.service.PresetSeanceService;
import com.example.dyeTrack.core.valueobject.IDNameValue;
import com.example.dyeTrack.core.valueobject.MuscleInfo;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;
import com.example.dyeTrack.in.exercise.dto.ExerciceDetailReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceLightReturnDTO;
import com.example.dyeTrack.in.exercise.dto.ExerciceReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetDetailReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetLightReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/preset-seances")
public class PresetSeanceController {

    private final PresetSeanceService presetSeanceService;

    public PresetSeanceController(PresetSeanceService presetSeanceService) {
        this.presetSeanceService = presetSeanceService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> save(@RequestBody @Valid PresetSeanceCreateRequestDTO presetSeanceCreateRequestDTO) {
        try {
            //start transaction 
            presetSeanceService.save(presetSeanceCreateRequestDTO.getName(),presetSeanceCreateRequestDTO.getIdUser(), presetSeanceCreateRequestDTO.getPresetSeanceExerciceVOs());      
            return ResponseEntity.status(HttpStatus.CREATED).body("Preset created successfully");
        } catch (ExerciseCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public List<? extends PresetReturnDTO> findAllOfUser(
            @RequestParam(defaultValue = "false") boolean ShowDetail,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long idUser) {

        List<PresetSeance> presetSeances = presetSeanceService.getAllPresetOfUser(idUser, name);

        if (!ShowDetail) {
            List<PresetLightReturnDTO> presetOut = new ArrayList<>();
            for (PresetSeance presetSeance : presetSeances) {
                presetOut.add(new PresetLightReturnDTO(presetSeance));
            }
            return presetOut;
        }

        List<PresetDetailReturnDTO> presetOut = new ArrayList<>();
        for (PresetSeance presetSeance : presetSeances) {

            // liste de VO pour ce preset
            List<PresetSeanceExerciceVO> voList = new ArrayList<>();

            for (PresetSeanceExercice pse : presetSeance.getPresetSeanceExercice()) {
                voList.add(new PresetSeanceExerciceVO(
                    pse.getExercice().getIdExercise(), 
                    pse.getParameter(),
                    pse.getRangeRepInf(),
                    pse.getRangeRepSup(),
                    pse.getLateralite() != null ? pse.getLateralite().getId() : null,
                    pse.getEquipement() != null ? pse.getEquipement().getId() : null
                ));
            }

            PresetDetailReturnDTO dto = new PresetDetailReturnDTO(presetSeance, voList);
            presetOut.add(dto);
        }

        return presetOut;
    }

     @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestParam Long idUser,
                                         @RequestParam String newName,
                                         @RequestBody @Valid List<PresetSeanceExercice> infoExerciePreset) {
        try {
            presetSeanceService.update(id, idUser, newName,infoExerciePreset);
            return ResponseEntity.ok("Exercise updated successfully");
        } catch (ExerciseCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
                                         @RequestParam Long idUser) {
        try {
            presetSeanceService.delete(id, idUser);
            return ResponseEntity.ok("Preset deleted successfully");
        } catch (ExerciseCreationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/getById/{id}")
    public  PresetReturnDTO getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showDetail,
            @RequestParam(required = false) Long idUser) {

        PresetSeance presetSeance = presetSeanceService.getById(id, idUser);

        if (!showDetail) {
            return new PresetLightReturnDTO(presetSeance);
        }

    List<PresetSeanceExerciceVO> voList = presetSeance.getPresetSeanceExercice().stream()
            .map(pse -> new PresetSeanceExerciceVO(
                    pse.getExercice().getIdExercise(), 
                    pse.getParameter(),
                    pse.getRangeRepInf(),
                    pse.getRangeRepSup(),
                    pse.getLateralite() != null ? pse.getLateralite().getId() : null,
                    pse.getEquipement() != null ? pse.getEquipement().getId() : null
            ))
            .toList();

    return new PresetDetailReturnDTO(presetSeance, voList);
    }

}
