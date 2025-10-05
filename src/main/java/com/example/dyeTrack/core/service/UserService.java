package com.example.dyeTrack.core.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.UserUseCase;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.HashUtil;
import com.example.dyeTrack.core.valueobject.AuthValue;
import com.example.dyeTrack.out.security.JWTService;

import jakarta.transaction.Transactional;

@Service
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final String emailSecretKey;
    private final JWTService jwtService;

    public UserService(UserPort userPort, PasswordEncoder passwordEncoder,
            @Value("${email.secret.key}") String emailSecretKey,
            JWTService jwtService) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.emailSecretKey = emailSecretKey;
        this.jwtService = jwtService;
    }

    public User get(Long id) {
        return userPort.get(id);
    }

    @Transactional
    public AuthValue save(String pseudo, String email, String password, LocalDate dateNaissance, Integer taille,
            Boolean sexeMale) {
        if (pseudo == null || pseudo.isBlank())
            throw new ForbiddenException("pseudo empty");
        if (email == null || email.isBlank())
            throw new ForbiddenException("email empty");
        if (password == null || password.isBlank())
            throw new ForbiddenException("password empty");
        if (taille != null && (taille < 50 || taille > 300))
            throw new ForbiddenException("taille Not realistic");

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(emailRegex, email)) {
            throw new ForbiddenException("email format invalid");
        }
        String hashedEmail = HashUtil.hashEmail(email, emailSecretKey);
        User userVerif = userPort.findByMailHashed(hashedEmail);
        if (userVerif != null)
            throw new ForbiddenException("Email déjà utilisé : " + email);
        String hashedPassword = passwordEncoder.encode(password);
        java.sql.Date sqlDateNaissance = dateNaissance != null ? java.sql.Date.valueOf(dateNaissance) : null;

        User user = userPort.save(new User(pseudo, hashedEmail, hashedPassword, new Date(System.currentTimeMillis()),
                sqlDateNaissance, taille, sexeMale));
        return new AuthValue(jwtService.generateToken(user.getId()), user);
    }

    @Transactional
    public AuthValue login(String email, String password) {
        if (email == null)
            throw new ForbiddenException("email empty");
        if (password == null)
            throw new ForbiddenException("password empty");
        if (email == null || email.isBlank())
            throw new ForbiddenException("email empty");
        if (password == null || password.isBlank())
            throw new ForbiddenException("password empty");
        String hashedEmail = HashUtil.hashEmail(email, emailSecretKey);
        User user = userPort.findByMailHashed(hashedEmail);
        if (user == null || !passwordEncoder.matches(password, user.getPassword()))
            throw new ForbiddenException("wrong password or email");
        return new AuthValue(jwtService.generateToken(user.getId()), user);
    }

    public User update(Long idTokenUser, Long idUser, String pseudo, String password,
            LocalDate dateNaissance, Integer taille, Boolean sexeMale) {
        if (!idTokenUser.equals(idUser))
            throw new ForbiddenException("Not same user");

        User user = userPort.get(idUser);
        if (user == null)
            throw new ForbiddenException("User not found");
        if (pseudo != null && !pseudo.isBlank())
            user.setPseudo(pseudo);
        if (password != null && !password.isBlank())
            user.setPassword(passwordEncoder.encode(password));
        if (dateNaissance != null)
            user.setDateNaissance(java.sql.Date.valueOf(dateNaissance));

        if (taille != null) {
            if (taille < 50 || taille > 300) {
                throw new ForbiddenException("taille not realistic");
            }
            user.setTaille(taille);
        }
        if (sexeMale != null)
            user.setSexeMale(sexeMale);
        return userPort.save(user);
    }

}
