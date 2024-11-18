package com.example.profileservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.profileservice.dto.request.UserProfileCreationRequest;
import com.example.profileservice.dto.response.UserProfileCreationResponse;
import com.example.profileservice.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @PostMapping("/users")
    UserProfileCreationResponse createProfile(@RequestBody UserProfileCreationRequest request) {
        return userProfileService.createProfile(request);
    }

    @GetMapping("/users/{profileId}")
    UserProfileCreationResponse getProfile(@PathVariable String profileId) {
        return userProfileService.getProfile(profileId);
    }
}
