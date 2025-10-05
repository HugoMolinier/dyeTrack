package com.example.dyeTrack.core.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(String id, String street, String number) {

}
