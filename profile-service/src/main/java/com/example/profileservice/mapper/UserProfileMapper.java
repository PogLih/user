package com.example.profileservice.mapper;

import com.example.profileservice.dto.request.UserProfileCreationRequest;
import com.example.profileservice.dto.response.UserProfileCreationResponse;
import com.example.profileservice.entity.UserProfile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

  UserProfile toUserProfile(UserProfileCreationRequest request);

  UserProfileCreationResponse toUserProfileResponse(UserProfile entity);
}
