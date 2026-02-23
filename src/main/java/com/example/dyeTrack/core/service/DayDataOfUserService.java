package com.example.dyeTrack.core.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Async;
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

        // Si les trois tracks sont null et qu'il existe une journée, on supprime
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
            var physioVO = dayDataOfUserVO.getPhysioTrack();
            if(physioVO.isEmpty()) {
                dayDataOfUser.setPhysioTrack(null);
            }else {
                PhysioTrack physio = dayDataOfUser.getPhysioTrack();
                if (physio == null) {
                    physio = new PhysioTrack(
                            physioVO.getWeight(),
                            physioVO.getStep(),
                            physioVO.getHourOfSleep(),
                            physioVO.getMood(),
                            dayDataOfUser);
                    dayDataOfUser.setPhysioTrack(physio);
                } else {
                    physio.setWeight(physioVO.getWeight());
                    physio.setStep(physioVO.getStep());
                    physio.setHourOfSleep(physioVO.getHourOfSleep());
                    physio.setMood(physioVO.getMood());
                }
            }
        }

        // Créer ou mettre à jour NutritionTrack
        if (dayDataOfUserVO.getNutritionTrack() != null) {
            var nutritionVO = dayDataOfUserVO.getNutritionTrack();
            if(nutritionVO.isEmpty()) {
                dayDataOfUser.setNutritionTrack(null);
            }else {
                NutritionTrack nutrition = dayDataOfUser.getNutritionTrack();
                if (nutrition == null) {
                    nutrition = new NutritionTrack(
                            nutritionVO.getCalories(),
                            nutritionVO.getProteins(),
                            nutritionVO.getLipids(),
                            nutritionVO.getCarbohydrates(),
                            nutritionVO.getFiber(),
                            nutritionVO.getCafeins(),
                            dayDataOfUser);
                    dayDataOfUser.setNutritionTrack(nutrition);
                } else {
                    nutrition.setCalories(nutritionVO.getCalories());
                    nutrition.setProteins(nutritionVO.getProteins());
                    nutrition.setLipids(nutritionVO.getLipids());
                    nutrition.setCarbohydrates(nutritionVO.getCarbohydrates());
                    nutrition.setFiber(nutritionVO.getFiber());
                    nutrition.setCafeins(nutritionVO.getCafeins());
                }
            }
        }
        if (dayDataOfUserVO.getSeanceTrack() != null) {
            SeanceTrack updatedSeance = seanceTrackService.createOrUpdateSeanceTrack(
                    dayDataOfUser,
                    idUser,
                    dayDataOfUserVO.getSeanceTrack());
            dayDataOfUser.setSeanceTrack(updatedSeance);
        }

        dayDataOfUser.setLastUpdate(Instant.now());
        return dayDataOfUserPort.save(dayDataOfUser);
    }
}