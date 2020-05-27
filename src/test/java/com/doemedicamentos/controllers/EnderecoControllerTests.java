package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.EnderecoService;
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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnderecoController.class)
@Import(JWTUtil.class)
public class EnderecoControllerTests {

    private static final String NUMERO_NAO_PODE_ESTAR_VAZIO = "o numero não pode estar Vazio";
    private static final String ENDERECO_NAO_PODE_ESTAR_VAZIO = "o endereco não pode estar Vazio" ;
    private static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado";


    @MockBean
    private EnderecoService enderecoService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    private Endereco endereco = new Endereco();

    @BeforeEach
    void setUp() {
        endereco.setIdEndereco(1);
        endereco.setEndereco("Alameda Jau");
        endereco.setNumero("100");
        endereco.setEstado("São Paulo");
        endereco.setCidade("São Paulo");
        endereco.setComplemento("apartamento 22");

    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarTodosEnderecos() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoService.buscarEnderecos()).thenReturn(enderecosIterable);

        String json = mapper.writeValueAsString(enderecosIterable);

        mockMvc.perform(MockMvcRequestBuilders.get("/endereco/todosenderecos"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarEnderecoOK() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(endereco));

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.get("/endereco/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.idEndereco", CoreMatchers.equalTo(1)));

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarEnderecoInexistente() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.get("/endereco/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarIncluirEndereco() throws Exception {
        Iterable<Endereco> enderecoIterable = Arrays.asList(endereco);
        Mockito.when(enderecoService.incluirEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.post("/endereco/incluir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isCreated())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.idEndereco", CoreMatchers.equalTo(1)));

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAlterarEndereco() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(endereco));

        Mockito.when(enderecoService.alterarEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.put("/endereco/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.idEndereco", CoreMatchers.equalTo(1)));


    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAlterarEnderecoInexistente() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        Mockito.when(enderecoService.alterarEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.put("/endereco/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(status().reason(ENDERECO_NAO_ENCONTRADO));


    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarExcluirEndereco() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.ofNullable(endereco));

        Mockito.when(enderecoService.excluirEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.delete("/endereco/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.idEndereco", CoreMatchers.equalTo(1)));


    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarExcluirEnderecoInexistente() throws Exception {
        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt()))
                .thenReturn(Optional.empty());

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.delete("/endereco/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isBadRequest())
                .andExpect(status().reason(ENDERECO_NAO_ENCONTRADO));


    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarEnderecoPorEstado() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoService.buscarPorEstado(endereco.getEstado())).thenReturn(enderecosIterable);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.put("/endereco/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isOk());

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarEnderecoPorEstadoECidade() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoService.buscarPorEstadoECidade(endereco.getEstado(), endereco.getCidade()))
                .thenReturn(enderecosIterable);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.put("/endereco/estadocidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isOk());

    }
    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarEnderecoPorCidade() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoService.buscarPorCidade(endereco.getCidade()))
                .thenReturn(enderecosIterable);

        String json = mapper.writeValueAsString(endereco);

        mockMvc.perform(MockMvcRequestBuilders.put("/endereco/cidade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers
                        .status().isOk());

    }

}
