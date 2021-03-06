package com.ponto.inteligente.api.controllers;

import com.ponto.inteligente.api.dtos.LancamentoDto;
import com.ponto.inteligente.api.entity.Funcionario;
import com.ponto.inteligente.api.entity.Lancamento;
import com.ponto.inteligente.api.entity.Response;
import com.ponto.inteligente.api.enums.TipoEnum;
import com.ponto.inteligente.api.services.FuncionarioService;
import com.ponto.inteligente.api.services.LancamentoService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import javax.validation.Valid;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private static final Logger LOG = LoggerFactory.getLogger(LancamentoController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController() {
    }

    /**
     * Retorna a listagem de lan??amentos de um funcion??rio.
     *
     * @param funcionarioId
     * @param pag
     * @param ord
     * @param dir
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {
        LOG.info("Buscando lan??amentos por ID do funcion??rio: {}, p??gina: {}", funcionarioId, pag);
        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest =  PageRequest.of(pag, this.qtdPorPagina, Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));

        response.setData(lancamentosDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna um lan??amento por ID.
     *
     * @param id
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        LOG.info("Buscando lan??amento por ID: {}", id);
        Response<LancamentoDto> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (!lancamento.isPresent()) {
            LOG.info("Lan??amento n??o encontrado para o ID: {}", id);
            response.getErros().add("Lan??amento n??o encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    /**
     * Adiciona um novo lan??amento.
     *
     * @param lancamentoDto
     * @param lancamento
     * @param result
     * @return ResponseEntity<Response<LancamentoDto>>
     * @throws java.text.ParseException
	 * @thr
     * ows ParseException
     */
    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
            BindingResult result) throws ParseException {
        LOG.info("Adicionando lan??amento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            LOG.error("Erro validando lan??amento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados de um lan??amento.
     *
     * @param id
     * @param lancamentoDto
     * @param result
     * @return ResponseEntity<Response<Lancamento>>
     * @throws java.text.ParseException
	 * @thr
     * ows ParseException
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        LOG.info("Atualizando lan??amento: {}", lancamentoDto.toString());
        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            LOG.error("Erro validando lan??amento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um lan??amento por ID.
     *
     * @param id
     * @return ResponseEntity<Response<Lancamento>>
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
        LOG.info("Removendo lan??amento: {}", id);
        Response<String> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (!lancamento.isPresent()) {
            LOG.info("Erro ao remover devido ao lan??amento ID: {} ser inv??lido.", id);
            response.getErros().add("Erro ao remover lan??amento. Registro n??o encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        this.lancamentoService.remover(id);
        return ResponseEntity.ok(new Response<>());
    }

    /**
     * Valida um funcion??rio, verificando se ele ?? existente e v??lido no
     * sistema.
     *
     * @param lancamentoDto
     * @param result
     */
    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
        if (lancamentoDto.getFuncionarioId() == null) {
            result.addError(new ObjectError("funcionario", "Funcion??rio n??o informado."));
            return;
        }

        LOG.info("Validando funcion??rio id {}: ", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
        if (!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcion??rio n??o encontrado. ID inexistente."));
        }
    }

    /**
     * Converte uma entidade lan??amento para seu respectivo DTO.
     *
     * @param lancamento
     * @return LancamentoDto
     */
    private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
        lancamentoDto.setTipo(lancamento.getTipo().toString());
        lancamentoDto.setDescricao(lancamento.getDescricao());
        lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
        lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());

        return lancamentoDto;
    }

    /**
     * Converte um LancamentoDto para uma entidade Lancamento.
     *
     * @param lancamentoDto
     * @param result
     * @return Lancamento
     * @throws ParseException
     */
    private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        Lancamento lancamento = new Lancamento();
        if (lancamentoDto.getId().isPresent()) {
            Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lan??amento n??o encontrado."));
            }
        } else {
            lancamento.setFuncionario(new Funcionario());
            lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
        }
        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));
        if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inv??lido."));
        }
        return lancamento;
    }

}
