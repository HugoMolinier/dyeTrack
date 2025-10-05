package com.example.dyeTrack.in.groupeMusculaire;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dyeTrack.core.entity.GroupeMusculaire;
import com.example.dyeTrack.core.service.GroupeMusculaireService;

@RestController
@RequestMapping("/api/GroupeMusculaire")
public class GroupeMusculaireController {
    private GroupeMusculaireService service;

    public GroupeMusculaireController(GroupeMusculaireService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<GroupeMusculaire> getAll() {
        return service.getAll();
    }
}
