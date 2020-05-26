package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Reserva;
import com.doemedicamentos.services.ReservaService;
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
@RequestMapping("/reserva")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<Reserva> criarReserva(@RequestBody @Validated Reserva reserva) throws InvalidAttributeValueException {
        try {
            Reserva reservaObjeto = reservaService.incluirReserva(reserva);
            return ResponseEntity.status(201).body(reservaObjeto);
        }catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    public Reserva buscarReservaPorId(@PathVariable Integer id){
        Optional<Reserva> reservaObjeto = reservaService.buscarReservaPorId(id);
        if(reservaObjeto.isPresent()){
            return reservaObjeto.get();
        }else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    public Reserva atualizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        reserva.setIdReserva(id);
        try {
            Reserva reservaObjeto = reservaService.alterarReserva(reserva);
            return reservaObjeto;
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("finalizar/{id}")
    public Reserva finalizarReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        reserva.setIdReserva(id);
        try {
            Reserva reservaObjeto = reservaService.finalizarReserva(reserva);
            return reservaObjeto;
        } catch (ObjectNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Reserva> excluirReserva(@PathVariable Integer id){
        Optional<Reserva> reservaOptional = reservaService.buscarReservaPorId(id);
        if(reservaOptional.isPresent()){
            reservaService.excluirSolicitacao(reservaOptional.get());
            return ResponseEntity.status(204).body(reservaOptional.get());
        }
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
