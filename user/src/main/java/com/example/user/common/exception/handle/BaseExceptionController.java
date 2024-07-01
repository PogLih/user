package com.example.user.common.exception.handle;

import com.example.user.common.exception.ApplicationException;
import com.example.user.common.response.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class BaseExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // Log the exception (you may want to use a logging framework like SLF4J)
        e.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse("An internal error occurred");

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<Object> handleException(ApplicationException ex, WebRequest request) {
        ResponseData responseData = new ResponseData();
        responseData.setResult_code(API_ERROR_RESULT_CODE);
        ValidateErrorObject error = new ValidateErrorObject();
        error.setErrorCode(
                !StringUtils.hasLength(ex.getErrorCode()) ? SmpRuleCode.ERR_SYS_001.getLabel() :
                        ex.getErrorCode());
        responseData.setValidateError(Arrays.asList(error));
        return super.handleExceptionInternal(ex, responseData, null, HttpStatus.OK, request);
        SuccessResponse.builder().build();
    }
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        FailureResponse failureResponse = new FailureResponse();
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        failureResponse.setErrors(errors);
//
//        return failureResponse.build();
//    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}