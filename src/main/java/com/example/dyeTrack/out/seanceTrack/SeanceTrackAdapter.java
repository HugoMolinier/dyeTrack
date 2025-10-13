package com.example.dyeTrack.out.seanceTrack;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.port.out.SeanceTrackPort;

@Component
public class SeanceTrackAdapter implements SeanceTrackPort {

    private final SeanceTrackRepository seanceTrackRepository;

    public SeanceTrackAdapter(SeanceTrackRepository seanceTrackRepository) {
        this.seanceTrackRepository = seanceTrackRepository;
    }

    @Override
    public SeanceTrack save(SeanceTrack seanceTrack) {
        return seanceTrackRepository.save(seanceTrack);
    }

    @Override
    public SeanceTrack getById(Long id) {
        return seanceTrackRepository.findById(id).orElse(null);
    }
}
