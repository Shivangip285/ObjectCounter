package com.example.ObjectCounter.model;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Data
public class ObjectCount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    private String objectName;

    @Positive
    private int count;
}
