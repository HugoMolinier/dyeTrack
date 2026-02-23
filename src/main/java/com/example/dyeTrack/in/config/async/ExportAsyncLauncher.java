package com.example.dyeTrack.in.config.async;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.example.dyeTrack.core.port.in.ExportDayDataUseCase;

@Component

public class ExportAsyncLauncher {

    private final ExportDayDataUseCase exportUseCase;

    public ExportAsyncLauncher(ExportDayDataUseCase exportUseCase) {
        this.exportUseCase = exportUseCase;
    }

    @Async
    @Transactional
    public void launch(Long exportRequestId) {
        exportUseCase.runExport(exportRequestId);
    }
}