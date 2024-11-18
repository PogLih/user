package com.example.profileservice.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileCreationResponse {

    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String city;
}
