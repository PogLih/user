package com.example.user.common.exception.handle;

import com.example.user.common.enums.ErrorCodeEnum;
import com.example.user.common.exception.ApplicationException;
import com.example.user.common.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BaseExceptionController extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        e.printStackTrace();
        return new FailureResponse().setErrorMessage("An internal error occurred").setErrorCode(ErrorCodeEnum.SYSTEM_ERROR).build();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleException(ApplicationException ex, WebRequest request) {
        ex.printStackTrace();
        return new FailureResponse().setErrorMessage("An internal error occurred").setErrorCode(ErrorCodeEnum.SYSTEM_ERROR).build();
    }

    //    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        FailureResponse<Map<String, String>> response =
//                FailureResponse.<Map<String, String>>builder().errors(errors).build();
//        return response.build();
//    }
}