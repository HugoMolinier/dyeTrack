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
        "AND (:officialExercise = TRUE AND e.user IS NULL OR :officialExercise = FALSE AND (:idUser IS NULL OR e.user.id = :idUser))"+
       "AND (:onlyPrincipalMuscle = FALSE OR rem.principal = TRUE)"+
       "AND (:idsMuscle IS NULL OR rem.muscle.id IN :idsMuscle)"+
        "AND (:idsExercices IS NULL OR e.idExercise IN :idsExercices)")
    List<Exercise> findAllFiltered(@Param("name") String name,
                                @Param("officialExercise") Boolean officialExercise,
                                @Param("idUser") Long idUser,
                                @Param("onlyPrincipalMuscle") Boolean onlyPrincipalMuscle,
                                @Param("idsMuscle") List<Integer> idsMuscle,
                                @Param("idsExercices") List<Long> idsExercices);

    // Avec groupe musculaire
    @Query("SELECT DISTINCT e FROM Exercise e " +
        "LEFT JOIN FETCH e.relExerciseMuscles rem " +
        "LEFT JOIN FETCH rem.muscle m " +
        "LEFT JOIN FETCH m.groupeMusculaire g " +
        "WHERE (:name IS NULL OR LOWER(e.nameFR) LIKE LOWER(CONCAT('%', :name, '%'))) " +
        "AND (:officialExercise = TRUE AND e.user IS NULL OR :officialExercise = FALSE AND (:idUser IS NULL OR e.user.id = :idUser)) " +
        "AND (:onlyPrincipalMuscle = FALSE OR rem.principal = TRUE)"+
        "AND (:idsGroupesMusculaire IS NULL OR g.id IN :idsGroupesMusculaire)"+
        "AND (:idsMuscle IS NULL OR m.id IN :idsMuscle)"+
        "AND (:idsExercices IS NULL OR e.idExercise IN :idsExercices)")
    List<Exercise> findAllFilteredWithGroup(
        @Param("name") String name,
        @Param("officialExercise") Boolean officialExercise,
        @Param("idUser") Long idUser,
        @Param("onlyPrincipalMuscle") Boolean onlyPrincipalMuscle,
        @Param("idsGroupesMusculaire") List<Integer> idsGroupesMusculaire,
        @Param("idsMuscle") List<Integer> idsMuscle,
        @Param("idsExercices") List<Long> idsExercice);



}
