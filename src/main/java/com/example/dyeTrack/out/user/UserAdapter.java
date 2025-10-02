package com.example.dyeTrack.out.user;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.port.out.UserPort;

@Component
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;

    public UserAdapter(UserRepository userRep){
        this.userRepository = userRep;
    }
    
    public User get(Long id){
        return userRepository.findById(id).orElse(null);

    }

    public List<User> getAll(){
        return userRepository.findAll();

    }
    
}
