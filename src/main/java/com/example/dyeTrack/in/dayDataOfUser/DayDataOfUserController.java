package com.example.dyeTrack.in.dayDataOfUser;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.service.DayDataOfUserService;
import com.example.dyeTrack.core.valueobject.DayDataOfUserVO;
import com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO.DayDataOfUserReturnDTO;
import com.example.dyeTrack.in.utils.ResponseBuilder;
import com.example.dyeTrack.in.utils.SecurityUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/daydata")
@SecurityRequirement(name = "bearerAuth")
public class DayDataOfUserController {

    private final DayDataOfUserService dayDataOfUserService;

    public DayDataOfUserController(DayDataOfUserService dayDataOfUserService) {
        this.dayDataOfUserService = dayDataOfUserService;
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All DataDay of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<List<DayDataOfUserReturnDTO>>> getAllDayData() {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        List<DayDataOfUser> days = dayDataOfUserService.getAllDayOfUser(idTokenUser);
        return ResponseBuilder.success(toDTOList(days), "Operation for getALl work");
    }

    @GetMapping("/getById/{id}")
    @Operation(summary = "Get by id DataDay of user", description = "Accessible only if a valid JWT is provided and corresponds to the user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<DayDataOfUserReturnDTO>> getById(@PathVariable Long id) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        DayDataOfUser day = dayDataOfUserService.getById(id, idTokenUser);
        return ResponseBuilder.success(toDTO(day), "Operation for getById work");
    }

    // Récupérer une journée spécifique
    @GetMapping("/getDay")
    public ResponseEntity<ResponseBuilder.ResponseDTO<DayDataOfUserReturnDTO>> getDayData(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();
        DayDataOfUser dayData = dayDataOfUserService.getDayDataOfUser(idTokenUser, day);
        return ResponseBuilder.success(toDTO(dayData), "dayData ");
    }

    @PostMapping("/save")
    @Operation(summary = "Create or update exercise info for the authenticated user")
    public ResponseEntity<ResponseBuilder.ResponseDTO<DayDataOfUserReturnDTO>> saveDayData(
            @RequestBody DayDataOfUserVO request) {
        Long idTokenUser = SecurityUtil.getUserIdFromContext();

        DayDataOfUser dayData = dayDataOfUserService.save(
                idTokenUser,
                request);

        if (dayData == null)
            return ResponseBuilder.success(null,
                    "DataDay delete avec succès");

        return ResponseBuilder.created(toDTO(dayData), "DataDay update Info");
    }

    // helper :

    private DayDataOfUserReturnDTO toDTO(DayDataOfUser entity) {
        if (entity == null)
            return null;
        return new DayDataOfUserReturnDTO(entity);
    }

    private List<DayDataOfUserReturnDTO> toDTOList(List<DayDataOfUser> entities) {
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }
}
