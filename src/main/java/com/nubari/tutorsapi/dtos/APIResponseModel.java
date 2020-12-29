package com.nubari.tutorsapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponseModel {
    private boolean isSuccessful;
    private String message;
}
