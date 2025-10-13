package com.example.dyeTrack.in.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.service.UserService;
import com.example.dyeTrack.in.user.dto.*;
import com.example.dyeTrack.in.utils.SecurityUtil;
import com.example.dyeTrack.in.utils.ResponseBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /* --- Récupération de l'utilisateur connecté --- */
    @GetMapping("/getUserConnected")
    @Operation(summary = "Get connected user information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ResponseBuilder.ResponseDTO<UserDTO>> getUser() {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        User user = service.get(idTokenUser);

        UserDTO dto = new UserDTO(
                user.getId(),
                user.getPseudo(),
                user.getDateRegister(),
                user.getBirthdate(),
                user.getHeight(),
                user.getSexeMale());

        return ResponseBuilder.success(dto, "Utilisateur récupéré avec succès");
    }

    /* --- Connexion d'un utilisateur --- */
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Allows an existing user to log in and receive a JWT token")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ReturnUserTokenDTO>> login(@RequestBody LoginUserDTO dto) {
        ReturnUserTokenDTO response = new ReturnUserTokenDTO(
                service.login(dto.getEmail(), dto.getPassword()));
        return ResponseBuilder.success(response, "Connexion réussie");
    }

    /* --- Inscription d'un nouvel utilisateur --- */
    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Allows a new user to register and receive a JWT token")
    public ResponseEntity<ResponseBuilder.ResponseDTO<ReturnUserTokenDTO>> register(
            @RequestBody @Valid RegisterUserDTO dto) {
        ReturnUserTokenDTO response = new ReturnUserTokenDTO(
                service.save(
                        dto.getPseudo(),
                        dto.getEmail(),
                        dto.getPassword(),
                        dto.getBirthdate(),
                        dto.getHeight(),
                        dto.getSexeMale()));
        return ResponseBuilder.created(response, "Utilisateur créé avec succès");
    }
}
