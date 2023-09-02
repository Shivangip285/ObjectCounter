package com.example.ObjectCounter.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
@Builder
@AllArgsConstructor
public class ObjectCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    private String objectName;

    @Positive
    private int count;

    public ObjectCount() {

    }
}
