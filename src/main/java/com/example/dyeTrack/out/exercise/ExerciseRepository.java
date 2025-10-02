package com.example.dyeTrack.out.exercise;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise,Long> {


@Query("SELECT DISTINCT e FROM Exercise e " +
       "LEFT JOIN FETCH e.relExerciseMuscles rem " +
       "WHERE e.idExercise = :idExercise")
Exercise findByIdExercise(@Param("idExercise") Long idExercise);

    
    @Query("SELECT DISTINCT e FROM Exercise e " +
        "LEFT JOIN FETCH e.relExerciseMuscles rem " +
        "WHERE (:name IS NULL OR LOWER(e.nameFR) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (:officialExercise = TRUE AND e.user IS NULL OR :officialExercise = FALSE AND (:idUser IS NULL OR e.user.id = :idUser))")
    List<Exercise> findAllFiltered(@Param("name") String name,
                                @Param("officialExercise") Boolean officialExercise,
                                @Param("idUser") Long idUser);


}
