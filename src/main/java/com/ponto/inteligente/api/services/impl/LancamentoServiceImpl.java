package com.ponto.inteligente.api.services.impl;

import com.ponto.inteligente.api.entity.Lancamento;
import com.ponto.inteligente.api.repositories.LancamentoRepository;
import com.ponto.inteligente.api.services.LancamentoService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private static final Logger LOG = LoggerFactory.getLogger(LancamentoServiceImpl.class);

    @Autowired
    private LancamentoRepository lancamentoRepository;
    
    
    @Override
    public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        LOG.info("Buscando lançamentos para o funcionário ID {}", funcionarioId);
        return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }
    
    @Override
    @Cacheable("lancamentoPorId")
    public Optional<Lancamento> buscarPorId(Long id) {
        LOG.info("Buscando um lançamento pelo ID {}", id);
        return this.lancamentoRepository.findById(id);
    }
    
    @Override
    @CachePut("lancamentoPorId")
    public Lancamento persistir(Lancamento lancamento) {
        LOG.info("Persistindo o lançamento: {}", lancamento);
        return this.lancamentoRepository.save(lancamento);
    }
    
    @Override
    public void remover(Long id) {
        LOG.info("Removendo o lançamento ID {}", id);
        this.lancamentoRepository.deleteById(id);
    }

}
