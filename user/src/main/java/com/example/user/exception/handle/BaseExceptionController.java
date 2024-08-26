package com.example.user.exception.handle;

import com.example.common_component.exception.ApplicationException;
import com.example.common_component.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseExceptionController extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseData handleException(Exception e) {
    e.printStackTrace();
    ResponseData responseData = new ResponseData();
    responseData.setData(e);
    return responseData;
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseData handleException(ApplicationException e, WebRequest request) {
    e.printStackTrace();
    ResponseData responseData = new ResponseData();
    responseData.setData(e);
    return responseData;
  }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseData handleValidationExceptions(MethodArgumentNotValidException ex) {
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