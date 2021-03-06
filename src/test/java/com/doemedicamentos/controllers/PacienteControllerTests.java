package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.EnderecoService;
import com.doemedicamentos.services.PacienteService;
import com.doemedicamentos.services.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hibernate.ObjectNotFoundException;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@WebMvcTest(PacienteController.class)
@Import(JWTUtil.class)
public class PacienteControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PacienteService pacienteService;
    @MockBean
    UsuarioService usuarioService;

    ObjectMapper mapper = new ObjectMapper();

    Paciente paciente;

    Endereco endereco;

    @BeforeEach
    public void Iniciar() throws ParseException {
        paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setDataNascimento(new SimpleDateFormat( "yyyyMMdd" ).parse( "20100520" ));
        paciente.setEmail("teste@gmail.com");
        paciente.setNome("Nome Paciente");
        paciente.setTelefone("11999999999");

        endereco = new Endereco();
        endereco.setIdEndereco(1);
        endereco.setEndereco("Rua Rio Grande do Norte");
        endereco.setNumero("170");
        endereco.setEstado("São Paulo");
        endereco.setCidade("Santo André");
        endereco.setComplemento("apartamento 42");
        paciente.setEndereco(endereco);
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarIncluirPaciente() throws Exception {

        Mockito.when(pacienteService.vincularEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        Mockito.when(pacienteService.incluirPaciente(Mockito.any(Paciente.class))).thenReturn(paciente);

        String json = mapper.writeValueAsString(paciente);

        mockMvc.perform(MockMvcRequestBuilders.post("/paciente")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.equalTo(paciente.getNome())))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarPacientePorId() throws Exception{

        Optional<Paciente> pacienteOptional = Optional.of(paciente);

        Mockito.when(pacienteService.buscarPacientePorId(Mockito.anyInt())).thenReturn(pacienteOptional);

        mockMvc.perform(MockMvcRequestBuilders.get("/paciente/1")
                )
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome",CoreMatchers.equalTo(paciente.getNome())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarPacienteIdInexistente() throws Exception {

        Optional<Paciente> pacienteOptional = Optional.empty();

        Mockito.when(pacienteService.buscarPacientePorId(Mockito.anyInt())).thenReturn(pacienteOptional);

        mockMvc.perform(MockMvcRequestBuilders.get("/paciente/1")
            )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAlterarPaciente() throws Exception{

        paciente.setNome("Teste Alterar Nome");

        Mockito.when(pacienteService.vincularEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        Mockito.when(pacienteService.alterarPaciente(Mockito.any(Paciente.class))).thenReturn(paciente);

        String json = mapper.writeValueAsString(paciente);

        mockMvc.perform(MockMvcRequestBuilders.put("/paciente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", CoreMatchers.equalTo(paciente.getNome())));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAlterarPacienteInexistente() throws Exception{

        paciente.setNome("Teste Alterar Nome");

        Mockito.when(pacienteService.alterarPaciente(Mockito.any(Paciente.class))).thenThrow(new ObjectNotFoundException(Paciente.class, "Paciente não encontrado."));

        String json = mapper.writeValueAsString(paciente);

        mockMvc.perform(MockMvcRequestBuilders.put("/paciente/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarExcluirPaciente() throws Exception {

        Optional optionalPaciente = Optional.of(paciente);

        Mockito.when(pacienteService.buscarPacientePorId(Mockito.anyInt())).thenReturn(optionalPaciente);

        pacienteService.excluirPaciente(paciente);
        Mockito.verify(pacienteService).excluirPaciente(Mockito.any(Paciente.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/paciente/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarExcluirPacienteInexistente() throws Exception {

        Mockito.when(pacienteService.buscarPacientePorId(Mockito.anyInt())).thenThrow(new ObjectNotFoundException(Paciente.class, "Paciente não Encontrado"));

        pacienteService.excluirPaciente(paciente);
        Mockito.verify(pacienteService).excluirPaciente(Mockito.any(Paciente.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/paciente/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}
