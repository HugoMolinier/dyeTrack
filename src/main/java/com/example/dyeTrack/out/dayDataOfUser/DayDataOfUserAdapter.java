package com.example.dyeTrack.out.dayDataOfUser;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.port.out.DayDataOfUserPort;

@Component
public class DayDataOfUserAdapter implements DayDataOfUserPort {
    private DayDataOfUserRepository dataOfUserRepository;

    public DayDataOfUserAdapter(DayDataOfUserRepository dataOfUserRepository) {
        this.dataOfUserRepository = dataOfUserRepository;
    }

    public List<DayDataOfUser> getAll(Long idUser) {
        return dataOfUserRepository.findByUserId(idUser);
    }

    public DayDataOfUser getById(Long idDaydataOfUser) {
        return dataOfUserRepository.findById(idDaydataOfUser).orElse(null);
    }

    public DayDataOfUser save(DayDataOfUser dayDataOfUser) {
        return dataOfUserRepository.save(dayDataOfUser);
    }

    public DayDataOfUser getDayDataOfUser(Long idUser, LocalDate dayData) {
        return dataOfUserRepository.findByUserIdAndDayData(idUser, dayData);

    }

    public void delete(Long id) {
        dataOfUserRepository.deleteById(id);

    }

}
