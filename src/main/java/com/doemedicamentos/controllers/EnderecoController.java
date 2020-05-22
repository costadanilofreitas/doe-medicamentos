package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.dtos.EstadoCidade;
import com.doemedicamentos.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    private static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado";

    @Autowired
    EnderecoService enderecoService;

    @GetMapping("/todosenderecos")
    public Iterable<Endereco> buscarTodosEnderecos() {
        return enderecoService.buscarEnderecos();
    }

    @GetMapping("/{idEndereco}")
    public Endereco buscarEndereco(@PathVariable Integer idEndereco) throws ResponseStatusException {
        Optional<Endereco> enderecoOptional = enderecoService.buscarPorid(idEndereco);
        if (enderecoOptional.isPresent()) {
            return enderecoOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/incluir")
    public ResponseEntity<Endereco> incluirEndereco(@RequestBody @Valid Endereco endereco) {
        enderecoService.incluirEndereco(endereco);
        return ResponseEntity.status(201).body(endereco);
    }

    @PutMapping("/{idEndereco}")
    public Endereco alterarEndereco(@PathVariable Integer idEndereco,
                                    @RequestBody @Valid Endereco endereco) throws ResponseStatusException {
        endereco.setIdEndereco(idEndereco);
        Optional<Endereco> enderecoOptional = enderecoService.buscarPorid(idEndereco);
        if (enderecoOptional.isPresent()) {
            Endereco enderecoObjeto = enderecoService.alterarEndereco(endereco);
            return enderecoObjeto;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ENDERECO_NAO_ENCONTRADO);
        }
    }

    @DeleteMapping("/{idEndereco}")
    public Endereco excluirEndereco(@PathVariable Integer idEndereco,
                                    @RequestBody @Valid Endereco endereco) {
        endereco.setIdEndereco(idEndereco);
        Optional<Endereco> enderecoOptional = enderecoService.buscarPorid(idEndereco);
        if (enderecoOptional.isPresent()) {
            enderecoService.excluirEndereco(endereco);
            return enderecoOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ENDERECO_NAO_ENCONTRADO);
        }

    }

    @PutMapping("/estado")
    public Iterable<Endereco> buscarEnderecoPorEstado(@RequestBody EstadoCidade estadoCidade) throws ResponseStatusException {
        return enderecoService.buscarPorEstado(estadoCidade.getEstado());
    }
    @PutMapping("/estadocidade")
    public Iterable<Endereco> buscarEnderecoPorEstadoECidade(@RequestBody EstadoCidade estadoCidade) throws ResponseStatusException {
        return enderecoService.buscarPorEstadoECidade(estadoCidade.getEstado(), estadoCidade.getCidade());
    }
    @PutMapping("/cidade")
    public Iterable<Endereco> buscarEnderecoPorCidade(@RequestBody EstadoCidade estadoCidade) throws ResponseStatusException {
        return enderecoService.buscarPorCidade(estadoCidade.getCidade());
    }

}
