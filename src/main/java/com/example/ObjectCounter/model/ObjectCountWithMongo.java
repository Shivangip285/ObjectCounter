package com.example.ObjectCounter.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Document()
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectCountWithMongo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    private String objectName;

    @Positive
    private int count;

}
