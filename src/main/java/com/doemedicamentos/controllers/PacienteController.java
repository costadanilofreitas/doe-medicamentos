package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/Paciente")
public class PacienteController {

    @Autowired
    PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<Paciente> incluirPaciente(@RequestBody Paciente paciente){

        Paciente pacienteObject = pacienteService.incluirPaciente(paciente);

        return ResponseEntity.status(201).body(pacienteObject);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscarPacientePorId(@PathVariable Integer id){

        try {
            Paciente paciente = pacienteService.buscarPacientePorId(id).get();
            return ResponseEntity.status(200).body(paciente);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Paciente> alterarPaciente(@RequestBody Paciente paciente, @PathVariable Integer id){

        try {
            paciente.setIdPaciente(id);
            Paciente pacienteObject = pacienteService.alterarPaciente(paciente);
            return ResponseEntity.status(200).body(pacienteObject);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Paciente> excluirPaciente(@PathVariable Integer id){

        try{
            Optional<Paciente> pacienteOptional = pacienteService.buscarPacientePorId(id);
            pacienteService.excluirPaciente(pacienteOptional.get());
            return ResponseEntity.status(204).body(null);
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
    }
}