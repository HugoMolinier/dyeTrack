package com.example.dyeTrack.core.port.out;

import java.util.List;

import com.example.dyeTrack.core.entity.User;

public interface UserPort {
    User get(Long id);
    List<User> getAll();
}
