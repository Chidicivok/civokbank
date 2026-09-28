package com.chidicivok.civokbank.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {

    private int statusCode;
    private String reason;
    private String name;
    private String message;
    private String path;
}