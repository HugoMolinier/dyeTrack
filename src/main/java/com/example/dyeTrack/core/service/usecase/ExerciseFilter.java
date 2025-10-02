package com.example.dyeTrack.core.service.usecase;

public class ExerciseFilter {
    private String name;
    private Boolean officialExercise;
    private Long idUser;
    private Boolean showMuscle;
    private Boolean onlyPrincipalMuscle;

    public ExerciseFilter() {}

    public ExerciseFilter(String name, Boolean officialExercise, Long idUser, Boolean showMuscle, Boolean onlyPrincipalMuscle) {
        this.name = name;
        this.officialExercise = officialExercise;
        this.idUser = idUser;
        this.showMuscle = showMuscle;
        this.onlyPrincipalMuscle = onlyPrincipalMuscle;
    }

    // Getters et setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getOfficialExercise() { return officialExercise; }
    public void setOfficialExercise(Boolean officialExercise) { this.officialExercise = officialExercise; }

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public Boolean getShowMuscle() { return showMuscle; }
    public void setShowMuscle(Boolean showMuscle) { this.showMuscle = showMuscle; }

    public Boolean getOnlyPrincipalMuscle() { return onlyPrincipalMuscle; }
    public void setOnlyPrincipalMuscle(Boolean onlyPrincipalMuscle) { this.onlyPrincipalMuscle = onlyPrincipalMuscle; }
}
