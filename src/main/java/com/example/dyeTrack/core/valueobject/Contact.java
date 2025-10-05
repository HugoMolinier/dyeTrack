package com.example.dyeTrack.core.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record Contact(String email) {

}
