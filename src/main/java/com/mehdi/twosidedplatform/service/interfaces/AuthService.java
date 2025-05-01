package com.mehdi.twosidedplatform.service.interfaces;

import com.mehdi.twosidedplatform.dto.AuthRequest;
import com.mehdi.twosidedplatform.dto.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    String login(AuthRequest request);
}
