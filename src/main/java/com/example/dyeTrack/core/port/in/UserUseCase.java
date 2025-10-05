package com.example.dyeTrack.core.port.in;

import java.time.LocalDate;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.valueobject.AuthValue;

public interface UserUseCase {
    User get(Long id);

    AuthValue save(String pseudo, String email, String password, LocalDate dateNaissance, Integer taille,
            Boolean sexeMale);

    AuthValue login(String email, String password);

    User update(Long idTokenUSer, Long idUser, String pseudo, String password, LocalDate dateNaissance, Integer taille,
            Boolean sexeMale);

}