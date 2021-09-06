package com.ponto.inteligente.api.security.services.impl;


import com.ponto.inteligente.api.security.entities.Usuario;
import com.ponto.inteligente.api.security.repositories.UsuarioRepository;
import com.ponto.inteligente.api.security.services.UsuarioService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return Optional.ofNullable(this.usuarioRepository.findByEmail(email));
    }
}
