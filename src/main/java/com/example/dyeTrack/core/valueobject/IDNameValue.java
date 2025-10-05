package com.example.dyeTrack.core.valueobject;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class IDNameValue {

    @NotNull
    private Long id;

    private String nameFR;

    private String nameEN;

    public IDNameValue() {
    }

    public IDNameValue(Long id, String nameFR, String nameEN) {
        this.id = id;
        this.nameFR = nameFR;
        this.nameEN = nameEN;
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameFR() {
        return nameFR;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String newNameEN) {
        this.nameEN = newNameEN;
    }

    public void setNameFR(String newNameFR) {
        this.nameFR = newNameFR;
    }

}
