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

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                HttpStatus.CONFLICT.name(),

                exception.getMessage(),

                httpServletRequest.getSession().getId(),
                httpServletRequest.getAuthType(),
                httpServletRequest.getServerPort(),
                httpServletRequest.getRequestURL().toString()

        );

        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                HttpStatus.NOT_FOUND.name(),

                exception.getMessage(),

                httpServletRequest.getSession().getId(),
                httpServletRequest.getAuthType(),
                httpServletRequest.getServerPort(),
                httpServletRequest.getRequestURL().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnAuthorizedPermissionException.class)
    public ResponseEntity<ApiError> handleUnAuthorizedPermissionException(UnAuthorizedPermissionException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                HttpStatus.FORBIDDEN.name(),

                exception.getMessage(),

                httpServletRequest.getSession().getId(),
                httpServletRequest.getAuthType(),
                httpServletRequest.getServerPort(),
                httpServletRequest.getRequestURL().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidArgumentException(InvalidArgumentException exception, HttpServletRequest httpServletRequest) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.name(),

                exception.getMessage(),

                httpServletRequest.getSession().getId(),
                httpServletRequest.getAuthType(),
                httpServletRequest.getServerPort(),
                httpServletRequest.getRequestURL().toString()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }



}
