package com.example.dyeTrack.in.infoExerciseUser;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;
import com.example.dyeTrack.core.service.InfoExerciseUserService;
import com.example.dyeTrack.in.infoExerciseUser.dto.UpdateInfoExerciseUserDTO;
import com.example.dyeTrack.in.infoExerciseUser.dto.out.ReturnInfoExerciseUserDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/info-exercise-user")
@SecurityRequirement(name = "bearerAuth")
public class InfoExerciseUserController {

    private final InfoExerciseUserService infoExerciseUserService;

    public InfoExerciseUserController(InfoExerciseUserService infoExerciseUserService) {
        this.infoExerciseUserService = infoExerciseUserService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Register Exercise of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ReturnInfoExerciseUserDTO>>> findAllOfUser(
            @RequestParam(defaultValue = "false") Boolean favorite,
            @RequestParam(defaultValue = "false") Boolean withNote) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<InfoExerciseUser> infoExerciseUsers = infoExerciseUserService.getAll(favorite, withNote, idTokenUser);
        return ResponseBuilder.success(
                infoExerciseUsers.stream()
                        .map(this::buildDto)
                        .toList(),
                "Liste des exercises utilisateur récupérée avec succès");
    }

    @PostMapping("/update/{id}")
    @Operation(summary = "Create or update exercise info for the authenticated user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ReturnInfoExerciseUserDTO>> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UpdateInfoExerciseUserDTO body) {

        Long userId = SecurityUtil.getUserIdFromContext();

        InfoExerciseUser updated = infoExerciseUserService.update(
                id,
                userId,
                body.getFavorite(),
                body.getNote());

        if (updated == null)
            return ResponseBuilder.success(null,
                    "Exercise delete avec succès");

        return ResponseBuilder.success(buildDto(updated), "Exercise mis à jour avec succès");
    }

    private ReturnInfoExerciseUserDTO buildDto(InfoExerciseUser infoExerciseUser) {
        return new ReturnInfoExerciseUserDTO(infoExerciseUser.getExercise().getIdExercise(), infoExerciseUser.getNote(),
                infoExerciseUser.isFavorite());

    }

}
