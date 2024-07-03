package com.example.user.common.response;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Log4j2
@Accessors(chain = true)
public abstract class BaseResponse {

    private HttpStatus status;
    private HttpHeaders headers = new HttpHeaders();
    private Map<String, Object> responsePayload = new HashMap<>();

    public BaseResponse(HttpStatus status) {
        this.status = status;
    }

    public BaseResponse setStatusCode(HttpStatus status) {
        this.status = status;
        return this;
    }

    public BaseResponse setMediaType(MediaType mediaType) {
        headers.setContentType(mediaType);
        return this;
    }

    public BaseResponse addHeader(String key, String value) {
        this.headers.add(key, value);
        return this;
    }

    public BaseResponse addHeader(String key, List<String> values) {
        this.headers.addAll(key, values);
        return this;
    }

    public BaseResponse setSuccess(boolean isSuccess) {
        this.addPayloadKey("success", isSuccess);
        return this;
    }

    public ResponseEntity<?> build() {
        return ResponseEntity
                .status(this.status)
                .headers(this.headers)
                .body(this.responsePayload);
    }

    protected void addPayloadKey(String key, Object value) {
        if (responsePayload.containsKey(key)) {
            log.debug("Response payload already exists field: {}. The value will be override", key);
        }
        this.responsePayload.put(key, value);
    }
}
