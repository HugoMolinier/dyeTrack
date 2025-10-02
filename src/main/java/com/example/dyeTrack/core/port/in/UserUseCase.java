package com.example.dyeTrack.core.port.in;

import java.util.List;

import com.example.dyeTrack.core.entity.User;

public interface UserUseCase {
    User get(Long id);
    List<User> getAll();
    
} 