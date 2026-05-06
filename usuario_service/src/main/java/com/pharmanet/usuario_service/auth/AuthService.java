package com.pharmanet.usuario_service.auth;

import org.springframework.stereotype.Service;

import com.pharmanet.usuario_service.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
