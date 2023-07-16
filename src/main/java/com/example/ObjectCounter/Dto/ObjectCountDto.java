package com.example.ObjectCounter.Dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public class ObjectCountDto {
    @NotBlank
    private String objectName;

    @Positive
    private int count;
}
