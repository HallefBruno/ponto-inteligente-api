package com.ponto.inteligente.api.services.impl;

import com.ponto.inteligente.api.entity.Funcionario;
import com.ponto.inteligente.api.repositories.FuncionarioRepository;
import com.ponto.inteligente.api.services.FuncionarioService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private static final Logger LOG = LoggerFactory.getLogger(FuncionarioServiceImpl.class);

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    public Funcionario persistir(Funcionario funcionario) {
        LOG.info("Persistindo funcionário: {}", funcionario);
        return this.funcionarioRepository.save(funcionario);
    }
    
    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        LOG.info("Buscando funcionário pelo CPF {}", cpf);
        return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }
    
    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        LOG.info("Buscando funcionário pelo email {}", email);
        return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
    }
    
    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        LOG.info("Buscando funcionário pelo IDl {}", id);
        return this.funcionarioRepository.findById(id);
    }

}
