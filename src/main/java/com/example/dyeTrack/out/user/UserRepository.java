package com.example.dyeTrack.out.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String hashedEmail);
}
