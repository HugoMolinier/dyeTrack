package com.example.dyeTrack.in.dayDataOfUser.dto.returnDTO;

import java.time.LocalDate;

public record ExportRequestDTO(
        LocalDate startDate,
        LocalDate endDate
) {}