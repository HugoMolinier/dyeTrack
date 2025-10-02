package com.example.dyeTrack.in.muscle;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.service.MuscleService;


@RestController
@RequestMapping("/api/muscle")
public class MuscleController {
    private MuscleService service;

    public MuscleController(MuscleService service){
        this.service = service;
    }

    @GetMapping("/")
    public List<Muscle> getAll() {
       return service.getAll();
    }

    @GetMapping("/{id}")
    public Muscle get(@PathVariable Long id) {
       return service.getById(id);
    }

   @GetMapping("/getByName/{name}")
    public List<Muscle> get(@PathVariable String name) {
       return service.getByName(name);
    }


    @GetMapping("/getByGroupe")
    public List<Muscle> getByGroupeMusculaire(@RequestParam  List<Integer> idDGroupeMuscle) {
       return service.getByIDGroupeMuscle(idDGroupeMuscle);
    }



    
    
}
