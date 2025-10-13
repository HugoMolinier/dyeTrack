package com.example.dyeTrack.core.port.in;

import java.time.LocalDate;
import java.util.List;

import com.example.dyeTrack.core.entity.DayDataOfUser;

import com.example.dyeTrack.core.valueobject.DayDataOfUserVO;

public interface DayDataOfUserUseCase {

    List<DayDataOfUser> getAllDayOfUser(Long idUser);

    DayDataOfUser save(Long idUser, DayDataOfUserVO dataOfUserVO);

    DayDataOfUser getById(Long id, Long idUser);

    DayDataOfUser getDayDataOfUser(Long idUser, LocalDate daydata);
}
