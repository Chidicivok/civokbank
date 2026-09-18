package com.chidicivok.civokbank.services.interfaces;

import com.chidicivok.civokbank.DTOs.requests.LoginRequest;

public interface AuthService {

    String login(LoginRequest request);
}