package com.pharmanet.usuario_service.Jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public String getToken(UserDetails usuario) {
        return getToken(new HashMap<>(), usuario);
    }

    public String getToken(Map<String, Object> extraClaims, UserDetails usuario ) {
        return Jwts
            .builder()
            .
    }
}
