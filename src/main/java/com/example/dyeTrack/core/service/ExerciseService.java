package com.example.dyeTrack.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.dyeTrack.core.entity.Exercise;
import com.example.dyeTrack.core.entity.Muscle;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.RelExerciseMuscle.RelExerciseMuscle;
import com.example.dyeTrack.core.exception.ExerciseCreationException;
import com.example.dyeTrack.core.port.in.ExerciseUseCase;
import com.example.dyeTrack.core.port.out.ExercisePort;
import com.example.dyeTrack.core.port.out.MusclePort;
import com.example.dyeTrack.core.port.out.RelExerciseMusclePort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.valueobject.MuscleInsertExercice;

import jakarta.transaction.Transactional;

@Service
public class ExerciseService implements ExerciseUseCase {
    private final ExercisePort exercisePort;
    private final MusclePort musclePort;
    private final UserPort userPort;
    private final RelExerciseMusclePort relExerciseMusclePort;

    public ExerciseService(ExercisePort exercisePort, UserPort userPort, RelExerciseMusclePort relExerciseMusclePort,
            MusclePort musclePort) {
        this.exercisePort = exercisePort;
        this.userPort = userPort;
        this.relExerciseMusclePort = relExerciseMusclePort;
        this.musclePort = musclePort;
    }

    public Exercise getByIdExercise(Long idExercise, Boolean onlyPrincipalMuscle) {
        Exercise e = exercisePort.getByIdExercise(idExercise);
        e.getRelExerciseMuscles().removeIf(rem -> !rem.isPrincipal());
        return e;
    }

    public List<Exercise> getAll(String name, Boolean officialExercise, Long idUser, Boolean onlyPrincipalMuscle,
            Boolean showMainFocusMuscularGroup, List<Integer> idsGroupeMuscle, List<Integer> idMuscle,
            List<Long> idsExercice) {
        if (showMainFocusMuscularGroup || idsGroupeMuscle != null) {
            return exercisePort.getAllWithShowGroupe(name, officialExercise, idUser, onlyPrincipalMuscle,
                    idsGroupeMuscle, idMuscle, idsExercice);
        }
        return exercisePort.getAll(name, officialExercise, idUser, onlyPrincipalMuscle, idMuscle, idsExercice);
    }

    @Transactional
    public void create(String nameFR, String description, String linkVideo, Long idUser,
            List<MuscleInsertExercice> relExerciseMuscles) {
        if (nameFR == null)
            throw new ExerciseCreationException("nameFR empty");
        if (idUser == null)
            throw new ExerciseCreationException("idUser empty");
        if (relExerciseMuscles == null || relExerciseMuscles.isEmpty())
            throw new ExerciseCreationException("La liste des muscles ne peut pas être vide");
        User user = userPort.get(idUser);
        if (user == null)
            throw new ExerciseCreationException("user Not found with id " + idUser);
        Exercise exercise = exercisePort.create(new Exercise(nameFR, description, linkVideo, user));
        List<RelExerciseMuscle> relExerciseMusclesArray = new ArrayList<RelExerciseMuscle>();
        int principalCount = 0;
        Map<Long, Boolean> musclePrincipalMap = new HashMap<>();
        for (MuscleInsertExercice muscleInfo : relExerciseMuscles) {
            Long muscleId = muscleInfo.getIdMuscle();
            Boolean existingPrincipal = musclePrincipalMap.get(muscleId);

            // Si le muscle existe déjà avec un statut différent, erreur
            if (existingPrincipal != null && !existingPrincipal.equals(muscleInfo.isPrincipal())) {
                throw new ExerciseCreationException(
                        "Le muscle " + muscleId + " ne peut pas être à la fois principal et non principal");
            }

            // Si le muscle n’a pas été ajouté encore, on l’ajoute
            if (existingPrincipal == null) {
                Muscle muscle = musclePort.getById(muscleId);
                if (muscle == null)
                    throw new ExerciseCreationException("muscle Not found with id " + muscleId);

                relExerciseMusclesArray.add(new RelExerciseMuscle(muscle, exercise, muscleInfo.isPrincipal()));
                musclePrincipalMap.put(muscleId, muscleInfo.isPrincipal());

                if (muscleInfo.isPrincipal())
                    principalCount++;
            }
        }

        if (principalCount != 1)
            throw new ExerciseCreationException("Il doit y avoir 1 muscle principal");

        relExerciseMusclePort.saveAll(relExerciseMusclesArray);
    }

    @Transactional
    public Exercise update(Long idExercice, Long idUserQuiModifie, String nameFR, String description, String linkVideo,
            List<MuscleInsertExercice> relExerciseMuscles) {
        if (idExercice == null)
            throw new ExerciseCreationException("idExercice empty");
        Exercise exercise = exercisePort.getByIdExercise(idExercice);
        if (exercise == null)
            throw new ExerciseCreationException("exercise Not found with id " + idExercice);
        if (exercise.getUser() == null)
            throw new ExerciseCreationException("Impossible de modifier un exercice officiel");
        if (!exercise.getUser().getId().equals(idUserQuiModifie))
            throw new ExerciseCreationException("Cet utilisateur ne peut pas modifier cet exercice");

        if (nameFR != null || description != null || linkVideo != null) {
            exercise.setNameFR(nameFR);
            exercise.setDescription(description);
            exercise.setLinkVideo(linkVideo);
            exercisePort.update(exercise);
        }

        // pour les muscles
        if (relExerciseMuscles != null && !relExerciseMuscles.isEmpty()) { // si vide on change rien
            relExerciseMusclePort.deleteByExerciceId(idExercice);
            // on delete puis reconstruit
            List<RelExerciseMuscle> relExerciseMusclesArray = new ArrayList<RelExerciseMuscle>();
            int principalCount = 0;
            for (MuscleInsertExercice muscleInfo : relExerciseMuscles) {
                Muscle muscle = musclePort.getById(muscleInfo.getIdMuscle());
                if (muscle == null)
                    throw new ExerciseCreationException("muscle Not found with id " + muscleInfo.getIdMuscle());
                relExerciseMusclesArray.add(new RelExerciseMuscle(muscle, exercise, muscleInfo.isPrincipal()));
                if (muscleInfo.isPrincipal())
                    principalCount++;
            }
            if (principalCount != 1)
                throw new ExerciseCreationException("Il doit y avoir 1 muscle principal");
            relExerciseMusclePort.saveAll(relExerciseMusclesArray);

        }
        return exercisePort.update(exercise);
    }

    @Transactional
    public void delete(Long idExercice, Long idUserQuiDelete) {
        if (idExercice == null)
            throw new ExerciseCreationException("idExercice empty");
        Exercise exercise = exercisePort.getByIdExercise(idExercice);
        if (exercise == null)
            throw new ExerciseCreationException("exercise Not found with id " + idExercice);
        if (exercise.getUser() == null)
            throw new ExerciseCreationException("Impossible de delete un exercice officiel");
        if (!exercise.getUser().getId().equals(idUserQuiDelete))
            throw new ExerciseCreationException("Cet utilisateur ne peut pas delete cet exercice");

        exercisePort.delete(exercise);
    }

}
