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
import com.example.dyeTrack.core.util.EntityUtils;
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

    @Override
    public User get(Long id) {
        return EntityUtils.getUserOrThrow(id, userPort);
    }

    @Transactional
    @Override
    public AuthValue save(String pseudo, String email, String password, LocalDate dateNaissance,
            Integer taille, Boolean sexeMale) {
        validateNewUserInputs(pseudo, email, password, taille);

        String hashedEmail = HashUtil.hashEmail(email, emailSecretKey);
        if (userPort.findByMailHashed(hashedEmail) != null) {
            throw new IllegalArgumentException("Email déjà utilisé : " + email);
        }

        User user = new User(
                pseudo,
                hashedEmail,
                passwordEncoder.encode(password),
                new Date(System.currentTimeMillis()),
                dateNaissance != null ? Date.valueOf(dateNaissance) : null,
                taille,
                sexeMale);

        user = userPort.save(user);
        return new AuthValue(jwtService.generateToken(user.getId()), user);
    }

    @Transactional
    @Override
    public AuthValue login(String email, String password) {
        if (isBlank(email) || isBlank(password)) {
            throw new IllegalArgumentException("Email ou mot de passe vide");
        }

        String hashedEmail = HashUtil.hashEmail(email, emailSecretKey);
        User user = userPort.findByMailHashed(hashedEmail);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenException("Email ou mot de passe incorrect");
        }

        return new AuthValue(jwtService.generateToken(user.getId()), user);
    }

    @Transactional
    @Override
    public User update(Long idTokenUser, Long idUser, String pseudo, String password,
            LocalDate dateNaissance, Integer taille, Boolean sexeMale) {

        if (!idTokenUser.equals(idUser)) {
            throw new ForbiddenException("Not same user");
        }

        User user = EntityUtils.getUserOrThrow(idUser, userPort);

        if (!isBlank(pseudo))
            user.setPseudo(pseudo);
        if (!isBlank(password))
            user.setPassword(passwordEncoder.encode(password));
        if (dateNaissance != null)
            user.setDateNaissance(Date.valueOf(dateNaissance));

        if (taille != null) {
            validateTaille(taille);
            user.setTaille(taille);
        }

        if (sexeMale != null)
            user.setSexeMale(sexeMale);

        return userPort.save(user);
    }

    /* --- Private helpers --- */

    private void validateNewUserInputs(String pseudo, String email, String password, Integer taille) {
        if (isBlank(pseudo))
            throw new IllegalArgumentException("Pseudo vide");
        if (isBlank(email))
            throw new IllegalArgumentException("Email vide");
        if (isBlank(password))
            throw new IllegalArgumentException("Mot de passe vide");
        validateEmailFormat(email);
        if (taille != null)
            validateTaille(taille);
    }

    private void validateEmailFormat(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(regex, email))
            throw new IllegalArgumentException("Email format invalid");
    }

    private void validateTaille(Integer taille) {
        if (taille < 50 || taille > 300)
            throw new IllegalArgumentException("Taille non réaliste");
    }

    private boolean isBlank(String str) {
        return str == null || str.isBlank();
    }
}
