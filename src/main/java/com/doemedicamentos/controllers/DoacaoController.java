package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.services.DoacaoService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.management.InvalidAttributeValueException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/doacao")
public class DoacaoController {

    @Autowired
    private DoacaoService doacaoService;

    @GetMapping("/{id}")
    public Doacao buscarDoacaoPorId(@PathVariable Integer id){
        Optional<Doacao> doacaoOptional = doacaoService.buscarDoacaoPorId(id);
        if(doacaoOptional.isPresent()){
            return doacaoOptional.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("buscarDoacaoPorMedicamento/{id}")
    public List<Doacao> buscarDoacaoPorMedicamentoId(@PathVariable Integer id){
        List<Doacao> doacoes = doacaoService.buscarDoacaoPorMedicamento(id);
        if(doacoes.size() > 0){
            return doacoes;
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping()
    public ResponseEntity<Iterable<Doacao>> buscarTodasDoacoes(){
        try {
            return ResponseEntity.status(200).body(doacaoService.buscarTodasDoacoes());
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Doacao> criarDoacao(@RequestBody @Validated Doacao doacao) throws InvalidAttributeValueException {
        Paciente paciente = doacaoService.buscarPacientePorId(doacao.getPaciente().getIdPaciente());
        doacao.setPaciente(paciente);
        Doacao doacaoObjeto = doacaoService.incluirDoacao(doacao);
        return ResponseEntity.status(201).body(doacaoObjeto);
    }

    @PutMapping("/{id}")
    public Doacao atualizarDoacao(@PathVariable Integer id, @RequestBody Doacao doacao) {
        doacao.setIdDocacao(id);
        try {
            Paciente paciente = doacaoService.buscarPacientePorId(doacao.getPaciente().getIdPaciente());
            doacao.setPaciente(paciente);
            Doacao doacaoObjeto = doacaoService.alterarDoacao(doacao);
            return doacaoObjeto;
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Doacao> deletarDoacao(@PathVariable Integer id){
        Optional<Doacao> doacaoOptional = doacaoService.buscarDoacaoPorId(id);
        if(doacaoOptional.isPresent()){
            doacaoService.excluirDoacao(doacaoOptional.get());
            return ResponseEntity.status(204).body(doacaoOptional.get());
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

}
