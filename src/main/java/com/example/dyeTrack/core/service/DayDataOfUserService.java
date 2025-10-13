package com.example.dyeTrack.core.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dyeTrack.core.entity.NutritionTrack;
import com.example.dyeTrack.core.entity.PhysioTrack;
import com.example.dyeTrack.core.entity.SeanceTrack;
import com.example.dyeTrack.core.entity.User;
import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.exception.EntityNotFoundException;
import com.example.dyeTrack.core.exception.ForbiddenException;
import com.example.dyeTrack.core.port.in.DayDataOfUserUseCase;
import com.example.dyeTrack.core.port.out.DayDataOfUserPort;
import com.example.dyeTrack.core.port.out.UserPort;
import com.example.dyeTrack.core.util.EntityUtils;
import com.example.dyeTrack.core.valueobject.DayDataOfUserVO;

@Service
public class DayDataOfUserService implements DayDataOfUserUseCase {

    private final DayDataOfUserPort dayDataOfUserPort;
    private final UserPort userPort;
    private final SeanceTrackService seanceTrackService;

    public DayDataOfUserService(DayDataOfUserPort dayDataOfUserPort, UserPort userPort,
            SeanceTrackService seanceTrackService) {
        this.dayDataOfUserPort = dayDataOfUserPort;
        this.userPort = userPort;
        this.seanceTrackService = seanceTrackService;
    }

    public DayDataOfUser getById(Long id, Long idUser) {
        DayDataOfUser dataOfUser = dayDataOfUserPort.getById(id);
        if (dataOfUser == null)
            throw new EntityNotFoundException("dataOfUser not found with id " + id);
        if (!dataOfUser.getUser().getId().equals(idUser))
            throw new ForbiddenException("Accès interdit");
        return dataOfUser;
    }

    public List<DayDataOfUser> getAllDayOfUser(Long idUser) {
        return dayDataOfUserPort.getAll(idUser);
    }

    public DayDataOfUser getDayDataOfUser(Long idUser, LocalDate dayData) {
        DayDataOfUser dataOfUser = dayDataOfUserPort.getDayDataOfUser(idUser, dayData);
        if (dataOfUser == null)
            throw new EntityNotFoundException("dataOfUser Not found with idUser " + idUser + " and date : " + dayData);
        return dataOfUser;
    }

    @Transactional
    public DayDataOfUser save(Long idUser, DayDataOfUserVO dayDataOfUserVO) {
        User user = EntityUtils.getUserOrThrow(idUser, userPort);

        // Vérifier si la journée existe déjà
        DayDataOfUser dayDataOfUser = dayDataOfUserPort.getDayDataOfUser(idUser, dayDataOfUserVO.getDayData());

        // Si les deux tracks sont null et qu'il existe une journée, on supprime
        if (dayDataOfUserVO.getPhysioTrack() == null && dayDataOfUserVO.getNutritionTrack() == null
                && dayDataOfUserVO.getSeanceTrack() == null) {
            if (dayDataOfUser != null) {
                dayDataOfUserPort.delete(dayDataOfUser.getIdDayData());
                return null;
            }
            throw new IllegalArgumentException("ALl the track is empty");
        }

        // Si la journée n'existe pas, on la crée
        if (dayDataOfUser == null) {
            dayDataOfUser = new DayDataOfUser(dayDataOfUserVO.getDayData(), user);
        }

        // Créer ou mettre à jour PhysioTrack
        if (dayDataOfUserVO.getPhysioTrack() != null) {
            PhysioTrack physio = dayDataOfUser.getPhysioTrack();
            if (physio == null) {
                physio = new PhysioTrack(
                        dayDataOfUserVO.getPhysioTrack().getWeight(),
                        dayDataOfUserVO.getPhysioTrack().getStep(),
                        dayDataOfUserVO.getPhysioTrack().getHourOfSleep(),
                        dayDataOfUserVO.getPhysioTrack().getMood(),
                        dayDataOfUser);
                dayDataOfUser.setPhysioTrack(physio);
            } else {
                physio.setWeight(dayDataOfUserVO.getPhysioTrack().getWeight());
                physio.setStep(dayDataOfUserVO.getPhysioTrack().getStep());
                physio.setHourOfSleep(dayDataOfUserVO.getPhysioTrack().getHourOfSleep());
                physio.setMood(dayDataOfUserVO.getPhysioTrack().getMood());
            }
        }

        // Créer ou mettre à jour NutritionTrack
        if (dayDataOfUserVO.getNutritionTrack() != null) {
            NutritionTrack nutrition = dayDataOfUser.getNutritionTrack();
            if (nutrition == null) {
                nutrition = new NutritionTrack(
                        dayDataOfUserVO.getNutritionTrack().getCalories(),
                        dayDataOfUserVO.getNutritionTrack().getProteins(),
                        dayDataOfUserVO.getNutritionTrack().getLipids(),
                        dayDataOfUserVO.getNutritionTrack().getCarbohydrates(),
                        dayDataOfUserVO.getNutritionTrack().getFiber(),
                        dayDataOfUserVO.getNutritionTrack().getCafeins(),
                        dayDataOfUser);
                dayDataOfUser.setNutritionTrack(nutrition);
            } else {
                nutrition.setCalories(dayDataOfUserVO.getNutritionTrack().getCalories());
                nutrition.setProteins(dayDataOfUserVO.getNutritionTrack().getProteins());
                nutrition.setLipids(dayDataOfUserVO.getNutritionTrack().getLipids());
                nutrition.setCarbohydrates(dayDataOfUserVO.getNutritionTrack().getCarbohydrates());
                nutrition.setFiber(dayDataOfUserVO.getNutritionTrack().getFiber());
                nutrition.setCafeins(dayDataOfUserVO.getNutritionTrack().getCafeins());
            }
        }
        if (dayDataOfUserVO.getSeanceTrack() != null) {
            SeanceTrack updatedSeance = seanceTrackService.createOrUpdateSeanceTrack(
                    dayDataOfUser,
                    idUser,
                    dayDataOfUserVO.getSeanceTrack());
            dayDataOfUser.setSeanceTrack(updatedSeance);
        }

        return dayDataOfUserPort.save(dayDataOfUser);
    }
}