package com.example.dyeTrack.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUser;
import com.example.dyeTrack.core.entity.infoExerciceUser.InfoExerciceUserId;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.InfoExerciceUserUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.InfoExerciceUserPort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;

@Service
public class InfoExerciceUserService implements InfoExerciceUserUseCase {

    private final InfoExerciceUserPort infoExerciceUserPort;
    private final UserPort userPort;
    private final ExercisePort exercisePort;

    public InfoExerciceUserService(InfoExerciceUserPort infoExerciceUserPort, UserPort userPort,
            ExercisePort exercisePort) {
        this.infoExerciceUserPort = infoExerciceUserPort;
        this.userPort = userPort;
        this.exercisePort = exercisePort;
    }

    public List<InfoExerciceUser> getAll(Boolean favorie, Boolean withNote, Long userId) {
        return infoExerciceUserPort.getAll(favorie, withNote, userId);

    };

    public InfoExerciceUser update(Long exerciceId, Long userId, Boolean favorie, String note) {
        validateInputs(userId, exerciceId);
        InfoExerciceUser existing = infoExerciceUserPort.getById(new InfoExerciceUserId(userId, exerciceId));

        if (existing == null) {
            User user = EntityUtils.getUserOrThrow(userId, userPort);
            Exercise exercise = EntityUtils.getExerciseOrThrow(exerciceId, exercisePort);
            if ((note == null || note.isBlank()) && (favorie == null || !favorie))
                throw new ForbiddenException(
                        "note " + note + " et favorie" + favorie + " pour un nouveau enregistrement" + note + favorie
                                + exerciceId + userId + new InfoExerciceUserId(userId, exerciceId));
            existing = new InfoExerciceUser(user, exercise, note, favorie);
        } else {
            if (note != null)
                existing.setNote(note);
            if (favorie != null)
                existing.setFavorie(favorie);
            if ((existing.getNote() == null || existing.getNote().isBlank())
                    && Boolean.FALSE.equals(existing.isFavorie())) {
                infoExerciceUserPort.delete(existing);
                return null;
            }
        }
        return infoExerciceUserPort.save(existing);
    }

    // Helper
    private void validateInputs(Long userId, Long exerciceId) {
        if (userId == null)
            throw new IllegalArgumentException("User ID cannot be null");
        if (exerciceId == null)
            throw new IllegalArgumentException("Exercise ID cannot be null");
    }

}
