package com.example.user.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
@Getter
@Setter
public class BaseRequest {
    private String requestId;
    private String requestType;
    private Map<String, String> headers;

}
