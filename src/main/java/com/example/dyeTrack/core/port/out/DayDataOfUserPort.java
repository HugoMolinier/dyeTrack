package com.example.dyeTrack.core.port.out;

import java.time.LocalDate;
import java.util.List;

import com.example.dyeTrack.core.entity.DayDataOfUser;

public interface DayDataOfUserPort {

    List<DayDataOfUser> getAll(Long idUser);

    DayDataOfUser save(DayDataOfUser dayDataOfUser);

    DayDataOfUser getDayDataOfUser(Long idUser, LocalDate dayData);

    DayDataOfUser getById(Long id);

    void delete(Long id);

}
