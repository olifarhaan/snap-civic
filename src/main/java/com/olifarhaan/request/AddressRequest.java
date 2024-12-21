package com.olifarhaan.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddressRequest {

    @NotBlank
    private String completeAddress;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
