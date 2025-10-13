package com.example.dyeTrack.core.port.in;

import java.util.Map;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.PlannedExercise;
import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.entity.setOfPlannedExercise.SetOfPlannedExercise;
import com.example.dyeTrack.core.valueobject.PlannedExerciseVO;
import com.example.dyeTrack.core.valueobject.SeanceTrackVO;
import com.example.dyeTrack.core.valueobject.SetOfPlannedExerciseVO;

public interface SeanceTrackUseCase {

    SeanceTrack createOrUpdateSeanceTrack(DayDataOfUser dayDataOfUser, Long userId, SeanceTrackVO vo);

    SeanceTrack getSeanceOfDay(Long dayDataOfUserId, Long userId);

}