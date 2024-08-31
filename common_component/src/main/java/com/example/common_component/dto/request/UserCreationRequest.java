package com.example.common_component.dto.request;

import com.example.common_component.anotations.DobConstraint;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

  @Size(min = 4, message = "USERNAME_INVALID")
  String username;

  @Size(min = 6, message = "INVALID_PASSWORD")
  String password;

  String firstName;
  String lastName;

  @DobConstraint(min = 10, message = "INVALID_DOB")
  LocalDate dob;
}
