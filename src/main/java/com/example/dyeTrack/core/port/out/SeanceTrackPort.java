package com.example.dyeTrack.core.port.out;

import com.example.dyeTrack.core.entity.SeanceTrack;

public interface SeanceTrackPort {

    SeanceTrack save(SeanceTrack vo);

    SeanceTrack getById(Long id);

}