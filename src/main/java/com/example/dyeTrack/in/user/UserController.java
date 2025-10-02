package com.example.dyeTrack.in.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.service.UserService;
import com.example.dyeTrack.in.user.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
       User user = service.get(id);
       if(user == null){return null;}
        return new UserDTO(user.getId(),user.getName());
    }


    @GetMapping("/")
    public List<UserDTO> getAll() {
       List<User> users = service.getAll();
       List<UserDTO> userOut =  new ArrayList<UserDTO>() ;
       for (User user : users){
            userOut.add(new UserDTO(user.getId(),user.getName()));
       }
       return userOut;
    
    }
    
    
}
