package com.ponto.inteligente.api.security.services;

import com.ponto.inteligente.api.security.entities.Usuario;
import java.util.Optional;


public interface UsuarioService {

    /**
     * Busca e retorna um usuário dado um email.
     *
     * @param email
     * @return Optional<Usuario>
     */
    Optional<Usuario> buscarPorEmail(String email);

}
