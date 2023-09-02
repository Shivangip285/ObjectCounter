package com.example.ObjectCounter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ObjectCountDto {
    @NotBlank
    private String objectName;

    @Positive
    private int count;
}
//http://modelmapper.org/user-manual/configuration/#matching-strategies
//https://www.baeldung.com/java-modelmapper
