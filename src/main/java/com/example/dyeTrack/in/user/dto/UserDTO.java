package com.example.dyeTrack.in.user.dto;

public class UserDTO {
    private Long id;
    private String name;


    public UserDTO (Long id, String name){
        this.id=id;
        this.name=name;

    }
    public Long getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }


    //...
    
}
