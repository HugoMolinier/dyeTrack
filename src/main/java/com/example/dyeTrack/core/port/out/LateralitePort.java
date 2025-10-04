package com.example.dyeTrack.core.port.out;

import java.util.List;
import com.example.dyeTrack.core.entity.Lateralite;

public interface LateralitePort {
    List<Lateralite> getAll();
    void save(Lateralite lateralite);
    
}
