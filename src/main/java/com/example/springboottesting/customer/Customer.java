package com.example.springboottesting.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = "id", allowGetters = true)
public class Customer {
  @Id @NotNull private UUID id;

  @NotBlank private String name;

  @NotBlank private String phoneNumber;
}
