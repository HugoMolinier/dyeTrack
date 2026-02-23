package com.example.dyeTrack.core.port.out;

import java.time.LocalDate;
import java.util.List;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.ExportRequest;

public interface ExportRequestPort {

    ExportRequest getById(Long id);

    ExportRequest save(ExportRequest request);

    void delete(ExportRequest request);

    List<DayDataOfUser> getDayDataBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    void generateAndSendExport(
            ExportRequest request,
            List<DayDataOfUser> data
    );
}