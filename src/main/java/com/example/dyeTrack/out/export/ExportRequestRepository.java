package com.example.dyeTrack.out.export;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.ExportRequest;

public interface ExportRequestRepository extends JpaRepository<ExportRequest, Long> {

    @Query("SELECT d FROM DayDataOfUser d " +
            "WHERE d.user.id = :userId " +
            "AND d.dayData BETWEEN :start AND :end "+
            "ORDER BY d.dayData ASC")
    List<DayDataOfUser> findDayDataBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDate startDate,
            @Param("end") LocalDate endDate
    );
}