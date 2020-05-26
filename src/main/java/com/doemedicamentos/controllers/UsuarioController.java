package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Usuario;
import com.doemedicamentos.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<Usuario> registarUsuario(@RequestBody @Valid Usuario usuario){
        Usuario userObj;
        try{
            userObj = usuarioService.salvarUsuario(usuario);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return ResponseEntity.status(201).body(userObj);

    }
    @GetMapping("/todosusuarios")
    public Iterable<Usuario> buscarTodosUsuarios() {
        return usuarioService.buscarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Integer id) throws ResponseStatusException {
        Optional<Usuario> usuarioOptional = usuarioService.buscarPorid(id);
        if (usuarioOptional.isPresent()) {
            return usuarioOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }


}
