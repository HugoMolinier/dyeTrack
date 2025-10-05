package com.example.dyeTrack.core.port.out;

import com.example.dyeTrack.core.entity.User;

public interface UserPort {
    User get(Long id);

    User save(User user);

    User findByMailHashed(String email);

    User update(User user);
}
