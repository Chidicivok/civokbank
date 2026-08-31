package com.chidicivok.civokbank.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
* Class Storing app error messages
* Based on HttpStatus Class, Exception, and HttpServletRequests
* */
@AllArgsConstructor
@Getter
public class ApiError {

    private int statusCode;
    private String reason;
    private String name;

    private String message;

    private String sessionId;
    private String authenticationType;
    private int serverPort;
    private String url;

}
