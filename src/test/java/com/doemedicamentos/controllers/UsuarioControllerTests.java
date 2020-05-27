package com.doemedicamentos.controllers;


import com.doemedicamentos.models.Usuario;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(JWTUtil.class)
public class UsuarioControllerTests {

    private static final String SENHA_CRIPT = "$2a$10$DBlkOowWRQtu5mT2u2fM6uA5JjcsoI9NRl.trA0oU9zM/6zJQ7HNa";

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    private Usuario usuario = new Usuario();

    @BeforeEach
    public void inicializar(){
        usuario.setId(1);
        usuario.setEmail("admin@gmail.com ");
        usuario.setNome("admin");
        usuario.setSenha(SENHA_CRIPT);

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarRegistarUsuario() throws Exception {
        Iterable<Usuario> usuarioIterable = Arrays.asList(usuario);
        Mockito.when(usuarioService.salvarUsuario(Mockito.any(Usuario.class))).thenReturn(usuario);

        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(MockMvcRequestBuilders.post("/usuario/registrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id", CoreMatchers.equalTo(1)));

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarTodosUsuarios() throws Exception {
        Iterable<Usuario> usuarioIterable = Arrays.asList(usuario);
        Mockito.when(usuarioService.buscarUsuarios()).thenReturn(usuarioIterable);

        String json = mapper.writeValueAsString(usuarioIterable);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuario/todosusuarios"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarUsuario() throws Exception {
        Mockito.when(usuarioService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(usuario));

        String json = mapper.writeValueAsString(usuario);

        mockMvc.perform(MockMvcRequestBuilders.get("/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.id", CoreMatchers.equalTo(1)));

    }

}
