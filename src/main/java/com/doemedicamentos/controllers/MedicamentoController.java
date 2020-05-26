package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.services.MedicamentoService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    MedicamentoService medicamentoService;

    @PostMapping
    public ResponseEntity<Medicamento> incluirMedicamento(@RequestBody @Valid Medicamento medicamento){

        Medicamento medicamentoObj = medicamentoService.incluirMedicamento(medicamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoObj);
    }

    @GetMapping
    public Iterable<Medicamento> buscarTodosMedicamentos(){
        return medicamentoService.buscarTodosMedicamentos();
    }

    @GetMapping("/{id}")
    public Medicamento buscarMedicamentoPorId(@PathVariable Integer id){

        Optional<Medicamento> medicamentoOptional = medicamentoService.buscarMedicamentoPorId(id);

        if (medicamentoOptional.isPresent()) {
            return medicamentoOptional.get();
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public Medicamento atualizarMedicamento(@PathVariable Integer id,@RequestBody @Valid Medicamento medicamentoAlterado){
        Medicamento medicamentoDB;
        medicamentoAlterado.setId(id);

        try {
            medicamentoDB = medicamentoService.atualizarMedicamento(medicamentoAlterado);
            return medicamentoDB;
        } catch (ObjectNotFoundException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Medicamento> deletarMedicamento(@PathVariable int id) {
        Optional<Medicamento> medicamentoOptional = medicamentoService.buscarMedicamentoPorId(id);
        if (medicamentoOptional.isPresent()){
            medicamentoService.deletarMedicamento(medicamentoOptional.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(medicamentoOptional.get());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Não encontrado registro solicitado.");
        }
    }

}
