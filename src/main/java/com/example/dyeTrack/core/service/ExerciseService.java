package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.ExerciseUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.MusclePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;

import jakarta.transaction.Transactional;

@Service
public class ExerciseService implements ExerciseUseCase {
    private final ExercisePort exercisePort;
    private final MusclePort musclePort;
    private final UserPort userPort;

    public ExerciseService(ExercisePort exercisePort, UserPort userPort,
            MusclePort musclePort) {
        this.exercisePort = exercisePort;
        this.userPort = userPort;
        this.musclePort = musclePort;
    }

    public Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle) {
        Exercise e = EntityUtils.getExerciseOrThrow(idExercise, exercisePort);
        e.getRelExerciseMuscles().removeIf(rem -> !rem.isPrincipal());
        return e;
    }

    public List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
            Boolean showMainFocusMuscularGroup, List<Integer> idsGroupeMuscle, List<Integer> idMuscle,
            List<Long> idsExercice) {
        return (showMainFocusMuscularGroup || idsGroupeMuscle != null)
                ? exercisePort.getAllWithShowGroupe(name, officialExercise, idUser, onlyPrincipalMuscle,
                        idsGroupeMuscle, idMuscle, idsExercice)
                : exercisePort.getAll(name, officialExercise, idUser, onlyPrincipalMuscle, idMuscle, idsExercice);
    }

    @Transactional
    public Exercise create(String nameFR, String description, String linkVideo, Long idUser,
            List<MuscleInsertExercice> relExerciseMuscles) {
        if (nameFR == null)
            throw new IllegalArgumentException("nameFR empty");
        if (idUser == null)
            throw new IllegalArgumentException("idUser empty");
        if (relExerciseMuscles == null || relExerciseMuscles.isEmpty())
            throw new IllegalArgumentException("La liste des muscles ne peut pas être vide");

        User user = EntityUtils.getUserOrThrow(idUser, userPort);

        Exercise exercise = new Exercise(nameFR, description, linkVideo, user);
        List<RelExerciseMuscle> relations = buildRelExerciseMuscles(exercise, relExerciseMuscles);
        exercise.getRelExerciseMuscles().addAll(relations);

        return exercisePort.create(exercise);
    }

    @Transactional
    public Exercise update(Long idExercice, Long idUserQuiModifie, String nameFR, String description, String linkVideo,
            List<MuscleInsertExercice> relExerciseMuscles) {

        Exercise exercise = EntityUtils.getExerciseOrThrow(idExercice, exercisePort);
        if (exercise.getUser() == null)
            throw new ForbiddenException("Impossible de modifier un exercice officiel");
        if (!exercise.getUser().getId().equals(idUserQuiModifie))
            throw new ForbiddenException("Cet utilisateur ne peut pas modifier cet exercice");

        if (nameFR != null || description != null || linkVideo != null) {
            exercise.setNameFR(nameFR);
            exercise.setDescription(description);
            exercise.setLinkVideo(linkVideo);
        }

        if (relExerciseMuscles != null && !relExerciseMuscles.isEmpty()) {
            List<RelExerciseMuscle> relations = buildRelExerciseMuscles(exercise, relExerciseMuscles);
            exercise.getRelExerciseMuscles().clear();
            exercise.getRelExerciseMuscles().addAll(relations);
        }

        return exercisePort.update(exercise);
    }

    @Transactional
    public void delete(Long idExercice, Long idUserQuiDelete) {

        Exercise exercise = EntityUtils.getExerciseOrThrow(idExercice, exercisePort);
        if (exercise.getUser() == null)
            throw new ForbiddenException("Impossible de delete un exercice officiel");
        if (!exercise.getUser().getId().equals(idUserQuiDelete))
            throw new ForbiddenException("Cet utilisateur ne peut pas delete cet exercice");
        exercisePort.delete(exercise);
    }

    // ============Utilitaire /
    private List<RelExerciseMuscle> buildRelExerciseMuscles(Exercise exercise,
            List<MuscleInsertExercice> relExerciseMuscles) {

        Map<Long, Muscle> muscleMap = musclePort.getAll().stream()
                .collect(Collectors.toMap(Muscle::getId, m -> m));

        Map<Long, Boolean> musclePrincipalMap = new HashMap<>();
        List<RelExerciseMuscle> relations = new ArrayList<>();
        int principalCount = 0;

        for (MuscleInsertExercice muscleInfo : relExerciseMuscles) {
            Long muscleId = muscleInfo.getIdMuscle();
            Boolean existingPrincipal = musclePrincipalMap.get(muscleId);

            if (existingPrincipal != null && !existingPrincipal.equals(muscleInfo.isPrincipal())) {
                throw new IllegalArgumentException(
                        "Le muscle " + muscleId + " ne peut pas être à la fois principal et non principal");
            }

            if (existingPrincipal == null) {
                Muscle muscle = muscleMap.get(muscleId);
                if (muscle == null)
                    throw new EntityNotFoundException("muscle Not found with id " + muscleId);

                relations.add(new RelExerciseMuscle(muscle, exercise, muscleInfo.isPrincipal()));
                musclePrincipalMap.put(muscleId, muscleInfo.isPrincipal());

                if (muscleInfo.isPrincipal())
                    principalCount++;
            }
        }
        if (principalCount != 1)
            throw new IllegalArgumentException("Il doit y avoir 1 muscle principal");

        return relations;
    }

}
