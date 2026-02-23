package com.example.dyeTrack.out.export;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.dyeTrack.core.entity.DayDataOfUser;
import com.example.dyeTrack.core.entity.ExportRequest;
import com.example.dyeTrack.core.port.out.ExportRequestPort;

@Repository
public class ExportRequestAdapter implements ExportRequestPort {

    private final ExportRequestRepository repository;

    public ExportRequestAdapter(ExportRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public ExportRequest getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public ExportRequest save(ExportRequest request) {
        return repository.save(request);
    }

    @Override
    public void delete(ExportRequest request){repository.delete(request);}
    @Override
    public List<DayDataOfUser> getDayDataBetween(Long userId, java.time.LocalDate startDate, java.time.LocalDate endDate) {
        return repository.findDayDataBetween(userId, startDate, endDate);
    }

    @Override
    public void generateAndSendExport(ExportRequest request, List<DayDataOfUser> data) {
        // ici tu fais le CSV + envoi mail
        System.out.println("Export CSV pour l'utilisateur " + request.getUser().getId() + " avec " + data.size() + " lignes");
    }
}