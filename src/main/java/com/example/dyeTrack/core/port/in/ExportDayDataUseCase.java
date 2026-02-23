package com.example.dyeTrack.core.port.in;

import com.example.dyeTrack.core.entity.ExportRequest;

import java.time.LocalDate;

public interface ExportDayDataUseCase {
    ExportRequest createExportRequest(Long userId, LocalDate startDate, LocalDate endDate);
    void runExport(Long exportRequestId);
}