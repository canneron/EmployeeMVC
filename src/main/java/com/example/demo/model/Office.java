package com.example.demo.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public record Office(@Id int officeId, @NotNull String officeLocation, @NotNull boolean airConditioned, @NotNull int capacity) {

}
