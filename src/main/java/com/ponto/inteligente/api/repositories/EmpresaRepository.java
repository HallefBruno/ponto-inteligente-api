package com.ponto.inteligente.api.repositories;

import com.ponto.inteligente.api.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    @Transactional(readOnly = true)
    Empresa findByCnpj(String cnpj);
}
