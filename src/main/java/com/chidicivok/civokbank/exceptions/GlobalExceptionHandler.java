package com.chidicivok.civokbank.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * Global Exception Handler Class to help ensure consistent error message throughout the application
 *
 * @RestControllerAdvice - used to define the class as a global exception handler for spring to use
 * */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*
     * @ExceptionHandler - controller level annotation for managing specific exceptions
     * */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResourceException(DuplicateResourceException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnAuthorizedPermissionException.class)
    public ResponseEntity<ApiError> handleUnAuthorizedPermissionException(UnAuthorizedPermissionException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.FORBIDDEN,
                exception.getMessage(),
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidArgumentException(InvalidArgumentException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }








    @ExceptionHandler(ExternalApiFailureException.class)
    public ResponseEntity<ApiError> handleWeatherServiceUnavailable(ExternalApiFailureException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.SERVICE_UNAVAILABLE,
                exception.getMessage(),
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(Exception exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = buildApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                httpServletRequest
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ApiError buildApiError(HttpStatus status, String message, HttpServletRequest httpServletRequest) {

        return new ApiError(
                status.value(),
                status.getReasonPhrase(),
                status.name(),
                message,
                httpServletRequest.getRequestURI()
        );
    }
}