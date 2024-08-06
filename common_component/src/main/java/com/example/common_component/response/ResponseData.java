package com.example.common_component.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ResponseData extends BaseResponse {

  Object data;
}
