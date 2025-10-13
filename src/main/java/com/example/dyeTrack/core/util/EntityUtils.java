package com.example.dyeTrack.core.util;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Lateralite;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.LateralitePort;
import com.example.dyeTrack.core.port.out.UserPort;

public final class EntityUtils {

    private EntityUtils() {
    }

    public static User getUserOrThrow(Long userId, UserPort userPort) {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("User ID must be provided and positive");

        User user = userPort.get(userId);
        if (user == null)
            throw new EntityNotFoundException("User not found with id " + userId);

        return user;
    }

    public static Exercise getExerciseOrThrow(Long exerciseId, ExercisePort exercisePort) {
        if (exerciseId == null || exerciseId <= 0)
            throw new IllegalArgumentException("Exercise ID must be provided and positive");

        Exercise exercise = exercisePort.getByIdExercise(exerciseId);
        if (exercise == null)
            throw new EntityNotFoundException("Exercise not found with id " + exerciseId);

        return exercise;
    }

}
