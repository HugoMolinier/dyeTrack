package com.example.dyeTrack.in.lateralite;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.service.LateraliteService;

@RestController
@RequestMapping("/api/Lateralite")
public class LateraliteController {
    private LateraliteService service;

    public LateraliteController(LateraliteService service){
        this.service = service;
    }

    @GetMapping("/")
    public List<Lateralite> getAll() {
       return service.getAll();
    }
}
