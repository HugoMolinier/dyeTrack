package com.example.dyeTrack.in.presetSeance;

import java.util.ArrayList;
import java.util.List;

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

import com.example.dyeTrack.core.entity.PresetSeance;
import com.example.dyeTrack.core.entity.PresetSeanceExercice.PresetSeanceExercice;
import com.example.dyeTrack.core.service.PresetSeanceService;
import com.example.dyeTrack.core.valueobject.PresetSeanceExerciceVO;
import com.example.dyeTrack.in.presetSeance.dto.PresetDetailReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetLightReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/preset-seances")
@SecurityRequirement(name = "bearerAuth")
public class PresetSeanceController {

    private final PresetSeanceService presetSeanceService;

    public PresetSeanceController(PresetSeanceService presetSeanceService) {
        this.presetSeanceService = presetSeanceService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create Preset attribuate to a user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<String> save(@RequestBody @Valid PresetSeanceCreateRequestDTO presetSeanceCreateRequestDTO,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        presetSeanceService.save(presetSeanceCreateRequestDTO.getName(), idTokenUser,
                presetSeanceCreateRequestDTO.getPresetSeanceExerciceVOs());
        return ResponseEntity.status(HttpStatus.CREATED).body("Preset created successfully");
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public List<? extends PresetReturnDTO> findAllOfUser(
            @RequestParam(defaultValue = "false") boolean ShowDetail,
            @RequestParam(required = false) String name,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        List<PresetSeance> presetSeances = presetSeanceService.getAllPresetOfUser(idTokenUser, name);

        if (!ShowDetail) {
            List<PresetLightReturnDTO> presetOut = new ArrayList<>();
            for (PresetSeance presetSeance : presetSeances) {
                presetOut.add(new PresetLightReturnDTO(presetSeance));
            }
            return presetOut;
        }

        List<PresetDetailReturnDTO> presetOut = new ArrayList<>();
        for (PresetSeance presetSeance : presetSeances) {
            List<PresetSeanceExerciceVO> voList = new ArrayList<>();

            for (PresetSeanceExercice pse : presetSeance.getPresetSeanceExercice()) {
                voList.add(new PresetSeanceExerciceVO(
                        pse.getExercice().getIdExercise(),
                        pse.getParameter(),
                        pse.getRangeRepInf(),
                        pse.getRangeRepSup(),
                        pse.getLateralite() != null ? pse.getLateralite().getId() : null,
                        pse.getEquipement() != null ? pse.getEquipement().getId() : null));
            }

            PresetDetailReturnDTO dto = new PresetDetailReturnDTO(presetSeance, voList);
            presetOut.add(dto);
        }

        return presetOut;
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<String> update(@PathVariable Long id,
            @RequestParam String newName,
            @RequestBody @Valid List<PresetSeanceExerciceVO> infoExerciePreset,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        presetSeanceService.update(id, idTokenUser, newName, infoExerciePreset);
        return ResponseEntity.ok("Exercise updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delelte a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<String> delete(@PathVariable Long id,
            HttpServletRequest request) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        presetSeanceService.delete(id, idTokenUser);
        return ResponseEntity.ok("Preset deleted successfully");
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "GetByID a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public PresetReturnDTO getById(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean showDetail,
            HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        PresetSeance presetSeance = presetSeanceService.getById(id, idTokenUser);

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
                        pse.getEquipement() != null ? pse.getEquipement().getId() : null))
                .toList();

        return new PresetDetailReturnDTO(presetSeance, voList);
    }

}
