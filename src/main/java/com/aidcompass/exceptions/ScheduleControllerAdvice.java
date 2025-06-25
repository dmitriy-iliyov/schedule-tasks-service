package com.aidcompass.exceptions;

import com.aidcompass.exceptions.models.base.BaseInternalServiceException;
import com.aidcompass.exceptions.models.base.BaseInvalidInputException;
import com.aidcompass.exceptions.models.base.BaseNotFoundException;
import com.aidcompass.exceptions.models.base.Exception;
import com.aidcompass.exceptions.models.dto.ErrorDto;
import com.aidcompass.exceptions.models.dto.ExceptionResponseDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.*;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ScheduleControllerAdvice {

    private final MessageSource messageSource;


    @ExceptionHandler({
            NoHandlerFoundException.class,
            NoResourceFoundException.class,
            org.springframework.web.HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<?> handleNoHandlerOrNoResourceFoundException(java.lang.Exception e) {
        String url = null;

        if (e instanceof NoHandlerFoundException) {
            url = ((NoHandlerFoundException) e).getRequestURL();
        }

        if (e instanceof NoResourceFoundException) {
            url = ((NoResourceFoundException) e).getResourcePath();
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponseDto(
                        "404",
                        "Not Found",
                        "The " + url + " was not found." + e.getClass())
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e, Locale locale) {
        log.error("Exception 500: {}", e.getMessage());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                messageSource.getMessage("500", null, "error.500", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(new ErrorDto("fromCode()", "Illegal argument exception!"))));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(Throwable throwable) {
        ExceptionResponseDto exceptionDto = new ExceptionResponseDto(
                "500",
                "Unexpected server error.",
                null);
        log.error(Arrays.toString(throwable.getStackTrace()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(Exception e){
        ExceptionResponseDto exceptionDto = new ExceptionResponseDto(
                "400",
                e.getMessage(),
                Arrays.toString(e.getStackTrace()));
        log.error("Exception 400: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionDto);
    }

    @ExceptionHandler(BaseNotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(BaseNotFoundException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                messageSource.getMessage("404", null, "error.404", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(e.getErrorDto())));
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e){
        ExceptionResponseDto exceptionDto = new ExceptionResponseDto(
                "500",
                "Illegal State Exception",
                null);
        log.error("Exception 500: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exceptionDto);
    }

    @ExceptionHandler(BaseInvalidInputException.class)
    public ResponseEntity<?> handleInvalidInputException(BaseInvalidInputException e, Locale locale){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("400", null, "error.400", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(e.getErrorDto())));
        System.out.println(problemDetail);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String pN = e.getParameterName();
        String message = String.valueOf(pN.charAt(0)).toUpperCase() + pN.substring(1) + " should be present!";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponseDto("404", message, null));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> handleHandlerMethodValidationException(HandlerMethodValidationException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("400", null, "error.400", locale));
        problemDetail.setProperty("properties", Map.of("errors", ErrorUtils.toErrorDtoList(e.getValueResults())));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("400", null, "error.400", locale));
        problemDetail.setProperty("properties", Map.of("errors", ErrorUtils.toErrorDtoList(e.getBindingResult())));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e, Locale locale) {
        Set<ConstraintViolation<?>> bindingResult = e.getConstraintViolations();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("400", null, "error.400", locale));
        problemDetail.setProperty("properties", Map.of("errors", ErrorUtils.toErrorDtoList(bindingResult)));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                messageSource.getMessage("400", null, "error.400", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(new ErrorDto(e.getName(), "Has invalid type."))));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, Locale locale) {

        Throwable root = getRootCause(e);

        if (root instanceof Exception exception) {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                    messageSource.getMessage("500", null, "error.500", locale));
            problemDetail.setProperty("properties", Map.of("errors", List.of(exception.getErrorDto())));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(problemDetail);
        }

        ProblemDetail generic = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Can't serialize request data!"
        );

        generic.setProperty("properties", Map.of("errors", e.getMessage().split(":")[0]));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(generic);
    }

    @ExceptionHandler(JsonMappingException.class)
    public ResponseEntity<?> handelJsonMappingException(JsonMappingException e, Locale locale) {
        Exception exception = (Exception) e.getCause();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage("500", null, "error.500", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(exception.getErrorDto())));
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(BaseInternalServiceException.class)
    public ResponseEntity<?> handleBaseInternalServiceException(BaseInternalServiceException e, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage("500", null, "error.500", locale));
        problemDetail.setProperty("properties", Map.of("errors", List.of(e.getErrorDto())));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problemDetail);
    }
}
