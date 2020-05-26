package com.doemedicamentos.services;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Usuario;
import com.doemedicamentos.repositories.UsuarioRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class UsuarioServiceTests {
    private static final String SENHA_CRIPT = "$2a$10$DBlkOowWRQtu5mT2u2fM6uA5JjcsoI9NRl.trA0oU9zM/6zJQ7HNa";
    @MockBean
    private UsuarioRepository usuarioRepository;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    private Usuario usuario = new Usuario();

    @BeforeEach
    public void inicializar(){
        usuario.setId(1);
        usuario.setEmail("admin@gmail.com ");
        usuario.setNome("admin");
        usuario.setSenha(SENHA_CRIPT);

    }
    @Test
    public void testarSalvarUsuario(){
        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(null);
        Mockito.when(bCryptPasswordEncoder.encode(usuario.getSenha())).thenReturn(SENHA_CRIPT);
        Mockito.when(usuarioRepository.save(Mockito.any())).thenReturn(usuario);
        Usuario usuarioObjeto = usuarioService.salvarUsuario(usuario);
        Assertions.assertEquals(usuarioObjeto, usuario);

    }

    @Test
    public void testarSalvarUsuarioExistente(){
        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(usuario);
        Assertions.assertThrows(RuntimeException.class,
                () -> usuarioService.salvarUsuario(usuario),
                "Esse email já existe");

    }
    @Test
    public void testarBuscarPorid(){
        Mockito.when(usuarioRepository.findById(Mockito.any())).thenReturn(Optional.of(usuario));
        usuarioService.buscarPorid(usuario.getId());
        Mockito.verify(usuarioRepository).findById(usuario.getId());

    }

    @Test public void testarBuscarUsuarios() throws Exception {
        Iterable<Usuario> usuarioIterable = Arrays.asList(usuario);
        Mockito.when(usuarioRepository.findAll())
                .thenReturn(usuarioIterable);
        Iterable<Usuario> usuarios = usuarioService.buscarUsuarios();
        Mockito.verify(usuarioRepository).findAll();
        Assertions.assertEquals(usuarios,usuarioIterable);

    }

    @Test
    public void testarLoadUserByUsernameInexistente(){
        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(null);
        Assertions.assertThrows(UsernameNotFoundException.class,
                () -> usuarioService.loadUserByUsername(usuario.getEmail()),
                "Esse email já existe");

    }

    @Test
    public void testarLoadUserByUsername(){
        Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(usuario);
        UserDetails userDetails = usuarioService.loadUserByUsername(usuario.getEmail());
        Assertions.assertEquals(userDetails.getUsername(), usuario.getEmail());
        Assertions.assertEquals(userDetails.getPassword(), usuario.getSenha());

    }

}
