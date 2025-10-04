package com.example.dyeTrack.out.presetSeance;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.PresetSeance;


public interface PresetSeanceRepository extends JpaRepository<PresetSeance,Long>{    

    @Query("SELECT DISTINCT p FROM PresetSeance p " +
           "LEFT JOIN FETCH p.presetSeanceExercices pse " +
           "WHERE p.user.id = :userId " +
           "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    List<PresetSeance> findAllPresetOfUser(
            @Param("userId") Long userId,
            @Param("name") String name
    );
}
