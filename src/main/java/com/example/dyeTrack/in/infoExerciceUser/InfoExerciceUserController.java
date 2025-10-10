package com.example.dyeTrack.in.infoExerciceUser;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;
import com.example.dyeTrack.core.service.InfoExerciceUserService;
import com.example.dyeTrack.in.infoExerciceUser.dto.UpdateInfoExerciceUserDTO;
import com.example.dyeTrack.in.infoExerciceUser.dto.out.ReturnInfoExerciceUserDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/info-exercise-user")
@SecurityRequirement(name = "bearerAuth")
public class InfoExerciceUserController {

    private final InfoExerciceUserService infoExerciceUserService;

    public InfoExerciceUserController(InfoExerciceUserService infoExerciceUserService) {
        this.infoExerciceUserService = infoExerciceUserService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Register Exercice of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<ReturnInfoExerciceUserDTO>>> findAllOfUser(
            @RequestParam(defaultValue = "false") Boolean favorite,
            @RequestParam(defaultValue = "false") Boolean withNote) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<InfoExerciceUser> infoExerciceUsers = infoExerciceUserService.getAll(favorite, withNote, idTokenUser);
        return ResponseBuilder.success(
                infoExerciceUsers.stream()
                        .map(this::buildDto)
                        .toList(),
                "Liste des exercices utilisateur récupérée avec succès");
    }

    @PostMapping("/{id}")
    @Operation(summary = "Create or update exercise info for the authenticated user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ReturnInfoExerciceUserDTO>> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UpdateInfoExerciceUserDTO body) {

        Long userId = SecurityUtil.getUserIdFromContext();

        InfoExerciceUser updated = infoExerciceUserService.update(
                id,
                userId,
                body.getFavorite(),
                body.getNote());

        if (updated == null)
            return ResponseBuilder.success(null,
                    "Exercice delete avec succès");

        return ResponseBuilder.success(buildDto(updated), "Exercice mis à jour avec succès");
    }

    private ReturnInfoExerciceUserDTO buildDto(InfoExerciceUser infoExerciceUser) {
        return new ReturnInfoExerciceUserDTO(infoExerciceUser.getExercice().getIdExercise(), infoExerciceUser.getNote(),
                infoExerciceUser.isFavorie());

    }

}
