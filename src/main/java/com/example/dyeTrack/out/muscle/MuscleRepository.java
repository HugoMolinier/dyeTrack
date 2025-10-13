package com.example.dyeTrack.out.muscle;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.Muscle;

import java.util.List;

public interface MuscleRepository extends JpaRepository<Muscle, Long> {

    @Query("SELECT m FROM Muscle m WHERE m.muscleGroup.id IN :ids")
    List<Muscle> findByMuscleGroupIds(@Param("ids") List<Integer> ids);

    @Query("SELECT m FROM Muscle m WHERE LOWER(m.nameFR) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(m.nameEN) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Muscle> findByName(@Param("name") String name);
}
