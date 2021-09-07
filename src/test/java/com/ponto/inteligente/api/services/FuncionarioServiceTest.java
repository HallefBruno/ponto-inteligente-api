package com.ponto.inteligente.api.services;

import com.ponto.inteligente.api.entity.Empresa;
import com.ponto.inteligente.api.entity.Funcionario;
import com.ponto.inteligente.api.enums.PerfilEnum;
import com.ponto.inteligente.api.repositories.EmpresaRepository;
import com.ponto.inteligente.api.utils.PasswordUtils;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FuncionarioServiceTest {

    @Autowired
    private FuncionarioService funcionarioService;
    
    @Autowired
    private EmpresaRepository empresaRepository;

    @Before
    public void setUp() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de exemplo");
        empresa.setCnpj("112344312343123");
        if(empresaRepository.findByCnpj(empresa.getCnpj()) == null)
            this.empresaRepository.save(empresa);

        Funcionario funcionario = new Funcionario();
        funcionario.setCpf("11111111111");
        if(!funcionarioService.buscarPorCpf("11111111111").isPresent()) {
            funcionario.setDataAtualizacao(new Date());
            funcionario.setDataCriacao(new Date());
            funcionario.setEmail("admin@email.com");
            funcionario.setEmpresa(this.empresaRepository.findByCnpj("112344312343123"));
            funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
            funcionario.setQtdHorasAlmoco(2.0f);
            funcionario.setQtdHorasTrabalhoDia(8.0f);
            funcionario.setValorHora(new BigDecimal("20.0"));
            funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
            funcionario.setNome("Milay veia");
            funcionarioService.persistir(funcionario);
        }
    }
    
    @Test
    public void testBuscarFuncionarioPorEmail() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("admin@email.com");
        Assert.assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarFuncionarioPorCpf() {
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("11111111111");
        Assert.assertTrue(funcionario.isPresent());
    }

}
