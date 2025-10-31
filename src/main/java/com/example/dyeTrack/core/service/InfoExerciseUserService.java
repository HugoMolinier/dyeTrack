package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUser;
import com.example.dyeTrack.core.entity.infoExerciseUser.InfoExerciseUserId;
import com.example.dyeTrack.core.port.in.InfoExerciseUserUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.InfoExerciseUserPort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;

@Service
public class InfoExerciseUserService implements InfoExerciseUserUseCase {

    private final InfoExerciseUserPort infoExerciseUserPort;
    private final UserPort userPort;
    private final ExercisePort exercisePort;

    public InfoExerciseUserService(InfoExerciseUserPort infoExerciseUserPort, UserPort userPort,
            ExercisePort exercisePort) {
        this.infoExerciseUserPort = infoExerciseUserPort;
        this.userPort = userPort;
        this.exercisePort = exercisePort;
    }

    public List<InfoExerciseUser> getAll(Long userId) {
        return infoExerciseUserPort.getAll(userId);

    };

    public InfoExerciseUser update(Long exerciseId, Long userId, Boolean favorite, String note) {
        validateInputs(userId, exerciseId);
        InfoExerciseUser existing = infoExerciseUserPort.getById(new InfoExerciseUserId(userId, exerciseId));

        if (existing == null) {
            User user = EntityUtils.getUserOrThrow(userId, userPort);
            Exercise exercise = EntityUtils.getExerciseOrThrow(exerciseId, exercisePort);
            if ((note == null || note.isBlank()) && (favorite == null || !favorite))
                throw new IllegalArgumentException(
                        "note " + note + " et favorite" + favorite + " pour un nouveau enregistrement" + note + favorite
                                + exerciseId + userId + new InfoExerciseUserId(userId, exerciseId));
            existing = new InfoExerciseUser(user, exercise, note, favorite);
        } else {
            if (note != null)
                existing.setNote(note);
            if (favorite != null)
                existing.setFavorite(favorite);
            if ((existing.getNote() == null || existing.getNote().isBlank())
                    && Boolean.FALSE.equals(existing.isFavorite())) {
                infoExerciseUserPort.delete(existing);
                return null;
            }
        }
        return infoExerciseUserPort.save(existing);
    }

    // Helper
    private void validateInputs(Long userId, Long exerciseId) {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
        if (exerciseId == null)
            throw new IllegalArgumentException("Exercise ID cannot be null");
    }

}
