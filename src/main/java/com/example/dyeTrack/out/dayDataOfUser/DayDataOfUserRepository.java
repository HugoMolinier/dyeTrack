package com.example.dyeTrack.out.dayDataOfUser;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dyeTrack.core.entity.DayDataOfUser;

public interface DayDataOfUserRepository extends JpaRepository<DayDataOfUser, Long> {
    List<DayDataOfUser> findByUserId(Long idUser);

    DayDataOfUser findByUserIdAndDayData(Long idUser, LocalDate dayData);
}
