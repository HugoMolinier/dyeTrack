package com.example.dyeTrack.in.presetSeance;

import java.util.ArrayList;
import java.util.List;

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

import com.example.dyeTrack.core.service.PresetSeanceService;
import com.example.dyeTrack.in.exercise.dto.ExerciceUltraLightReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetDetailReturnDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceCreateRequestDTO;
import com.example.dyeTrack.in.presetSeance.dto.PresetSeanceExerciceVODTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/preset-seances")
@SecurityRequirement(name = "bearerAuth")
public class PresetSeanceController {

    private final PresetSeanceService presetSeanceService;

    public PresetSeanceController(PresetSeanceService presetSeanceService) {
        this.presetSeanceService = presetSeanceService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create Preset attribuate to a user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<PresetDetailReturnDTO>> create(
            @RequestBody @Valid PresetSeanceCreateRequestDTO presetSeanceCreateRequestDTO) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        return ResponseBuilder
                .created(buildDetailDTO(presetSeanceService.save(presetSeanceCreateRequestDTO.getName(), idTokenUser,
                        presetSeanceCreateRequestDTO.getPresetSeanceExerciceVOs())), "Preset créé avec succès");

    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<PresetDetailReturnDTO>>> findAllOfUser(
            @RequestParam(required = false) String name) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        List<PresetSeance> presetSeances = presetSeanceService.getAllPresetOfUser(idTokenUser, name);

        List<PresetDetailReturnDTO> presetOut = new ArrayList<>();
        for (PresetSeance presetSeance : presetSeances) {
            presetOut.add(buildDetailDTO(presetSeance));
        }

        return ResponseBuilder.success(presetOut, "Liste des presets récupérée avec succès");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<PresetDetailReturnDTO>> update(@PathVariable Long id,
            @RequestBody PresetSeanceCreateRequestDTO presetSeanceCreateRequestDTO) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        return ResponseBuilder
                .success(
                        buildDetailDTO(
                                presetSeanceService.update(id, idTokenUser, presetSeanceCreateRequestDTO.getName(),
                                        presetSeanceCreateRequestDTO.getPresetSeanceExerciceVOs())),
                        "Preset mis à jour avec succès");

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delelte a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<String>> delete(@PathVariable Long id) {

        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        presetSeanceService.delete(id, idTokenUser);
        return ResponseBuilder.success(null, "Preset supprimé avec succès");
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "GetByID a Preset of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<PresetDetailReturnDTO>> getById(
            @PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        return ResponseBuilder.success(buildDetailDTO(presetSeanceService.getById(id, idTokenUser)),
                "Preset récupéré avec succès");

    }

    // Helper
    private PresetDetailReturnDTO buildDetailDTO(PresetSeance presetSeance) {

        List<PresetSeanceExerciceVODTO> voList = presetSeance.getPresetSeanceExercice().stream()
                .map(pse -> new PresetSeanceExerciceVODTO(
                        new ExerciceUltraLightReturnDTO(pse.getExercice()),
                        pse.getParameter(),
                        pse.getRangeRepInf(),
                        pse.getRangeRepSup(),
                        pse.getOrderExercice(),
                        pse.getLateralite(),
                        pse.getEquipement()))
                .toList();

        return new PresetDetailReturnDTO(presetSeance, voList);
    }

}
