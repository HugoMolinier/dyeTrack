package com.example.dyeTrack.core.entity;

import com.example.dyeTrack.core.valueobject.ExportStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "export_request")
public class ExportRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExportStatus statut;

    @ManyToOne
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;


    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    private LocalDateTime finishedAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.statut = ExportStatus.PENDING;
    }

    public void markRunning() {
        this.statut = ExportStatus.RUNNING;
    }

    public void markDone() {
        this.statut = ExportStatus.DONE;
        this.finishedAt = LocalDateTime.now();
    }

    public void markError(String error) {
        this.statut = ExportStatus.ERROR;
        this.errorMessage = error;
        this.finishedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public ExportStatus getStatut() {
        return statut;
    }

    public User getUser() {
        return user;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setStatut(ExportStatus statut) {
        this.statut = statut;
    }
}
