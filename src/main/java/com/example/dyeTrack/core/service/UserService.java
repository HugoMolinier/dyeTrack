package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.port.in.UserUseCase;
import com.example.dyeTrack.core.port.out.UserPort;

@Service
public class UserService implements UserUseCase {
     
    private UserPort userPort;
    
    public UserService(UserPort UserPort){
        this.userPort = UserPort;
    }

    public User get(Long id){
        return userPort.get(id);
    }

    public List<User> getAll(){
        return userPort.getAll();
    }

}

