package com.ponto.inteligente.api.services.impl;

import com.ponto.inteligente.api.entity.Empresa;
import com.ponto.inteligente.api.repositories.EmpresaRepository;
import com.ponto.inteligente.api.services.EmpresaService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger LOG = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        LOG.info("Buscando uma empresa para o CNPJ {}", cnpj);
        return Optional.ofNullable(empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        LOG.info("Persistindo empresa: {}", empresa);
        return this.empresaRepository.save(empresa);
    }

}
