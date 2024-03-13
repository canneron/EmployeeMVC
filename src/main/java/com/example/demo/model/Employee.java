package com.example.demo.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    int employeeId;
    @NotBlank(message = "First name required")
    @Getter
    @Setter
    String firstName;
    @NotNull (message = "Last name required")
    @Getter
    @Setter
    String lastName;
    @NotNull (message = "Title required")
    @Getter
    @Setter
    String title;
    @NotNull (message = "Access Level required")
    @Getter
    @Setter
    String access;

}
