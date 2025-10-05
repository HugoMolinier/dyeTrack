package com.example.dyeTrack.in.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import com.example.dyeTrack.in.user.dto.LoginUserDTO;
import com.example.dyeTrack.in.user.dto.RegisterUserDTO;
import com.example.dyeTrack.in.user.dto.ReturnUserTokenDTO;
import com.example.dyeTrack.in.user.dto.UserDTO;
import com.example.dyeTrack.in.utils.SecurityUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/getUserConnected")
    @Operation(summary = "Get connected user information", description = "Accessible only if a valid JWT is provided and corresponds to the user", security = @SecurityRequirement(name = "bearerAuth"))
    public UserDTO getUser(HttpServletRequest request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        if (idTokenUser == null)
            return null;
        User user = service.get(idTokenUser);
        return user == null ? null
                : new UserDTO(
                        user.getId(),
                        user.getPseudo(),
                        user.getDateRegister(),
                        user.getDateNaissance(),
                        user.getTaille(),
                        user.getSexeMale());
    }

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Allows an existing user to log in and receive a JWT token")
    public ReturnUserTokenDTO login(
            @RequestBody LoginUserDTO dto) {
        return new ReturnUserTokenDTO(service.login(dto.getEmail(), dto.getPassword()));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration", description = "Allows a new user to register and receive a JWT token")
    public ReturnUserTokenDTO register(@RequestBody @Valid RegisterUserDTO dto) {
        return new ReturnUserTokenDTO(service.save(dto.getPseudo(), dto.getEmail(), dto.getPassword(),
                dto.getDateNaissance(), dto.getTaille(), dto.getSexeMale()));
    }

}
