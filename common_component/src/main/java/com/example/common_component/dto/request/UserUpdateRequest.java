package com.example.common_component.dto.request;

import com.example.common_component.anotations.DobConstraint;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

  String password;
  String firstName;
  String lastName;

  @DobConstraint(min = 18, message = "INVALID_DOB")
  LocalDate dob;

  List<String> roles;
}
