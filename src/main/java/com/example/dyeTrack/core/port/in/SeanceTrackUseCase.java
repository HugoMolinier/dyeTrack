package com.example.dyeTrack.core.port.in;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.valueobject.SeanceTrackVO;

public interface SeanceTrackUseCase {

    SeanceTrack createOrUpdateSeanceTrack(DayDataOfUser dayDataOfUser, Long userId, SeanceTrackVO vo);

    SeanceTrack getSeanceOfDay(Long dayDataOfUserId, Long userId);

}