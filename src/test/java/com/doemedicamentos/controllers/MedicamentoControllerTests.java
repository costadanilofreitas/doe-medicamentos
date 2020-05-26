package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.MedicamentoService;
import com.doemedicamentos.services.UsuarioService;
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

import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(MedicamentoController.class)
@Import(JWTUtil.class
)
public class MedicamentoControllerTests {
    @MockBean
    MedicamentoService medicamentoService;
    @MockBean
    UsuarioService usuarioService;


    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();
    Medicamento medicamento;

    @BeforeEach
    public void inicializar(){
        medicamento = new Medicamento();
        medicamento.setId(1);
        medicamento.setNome("Aspirina");
        medicamento.setLaboratorio("Bayer");
        medicamento.setControlado(false);
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarIncluirMedicamento() throws Exception {

        Mockito.when(medicamentoService.incluirMedicamento(Mockito.any(Medicamento.class))).thenReturn(medicamento);

        String json = mapper.writeValueAsString(medicamento);

        mockMvc.perform(MockMvcRequestBuilders.post("/medicamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(1)));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarTodosMedicamentos() throws Exception {

        Iterable<Medicamento> medicamentoIterable = Arrays.asList(medicamento);

        Mockito.when(medicamentoService.buscarTodosMedicamentos()).thenReturn(medicamentoIterable);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicamentos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id",CoreMatchers.equalTo(medicamento.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].nome",CoreMatchers.equalTo(medicamento.getNome())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].laboratorio",CoreMatchers.equalTo(medicamento.getLaboratorio())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].controlado",CoreMatchers.equalTo(medicamento.isControlado())));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarNaoEncontrarBuscarTodosMedicamentos() throws Exception {
        Iterable<Medicamento> medicamentoIterable = Arrays.asList();

        Mockito.when(medicamentoService.buscarTodosMedicamentos()).thenReturn(medicamentoIterable);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$" ).isEmpty());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarMedicamentoPorId() throws Exception {
        Optional<Medicamento> medicamentoOptional = Optional.of(medicamento);

        Mockito.when(medicamentoService.buscarMedicamentoPorId(Mockito.anyInt())).thenReturn(medicamentoOptional);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.equalTo(medicamento.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome",CoreMatchers.equalTo(medicamento.getNome())));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarNaoEncontrarBuscarMedicamentoPorId() throws Exception {
        Optional<Medicamento> medicamentoOptional = Optional.empty();

        Mockito.when(medicamentoService.buscarMedicamentoPorId(Mockito.anyInt())).thenReturn(medicamentoOptional);

        mockMvc.perform(MockMvcRequestBuilders.get("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$" ).doesNotExist());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAtualizarMedicamento() throws Exception {

        Mockito.when(medicamentoService.atualizarMedicamento(Mockito.any(Medicamento.class))).thenReturn(medicamento);

        String json = mapper.writeValueAsString(medicamento);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",CoreMatchers.equalTo(medicamento.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome",CoreMatchers.equalTo(medicamento.getNome())));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarNaoEncontrarAtualizarMedicamento() throws Exception {

        Optional<Medicamento> medicamentoOptional = Optional.empty();

        Mockito.when(medicamentoService.buscarMedicamentoPorId(Mockito.anyInt())).thenReturn(medicamentoOptional);

        Mockito.when(medicamentoService.atualizarMedicamento(Mockito.any(Medicamento.class))).thenThrow(new ObjectNotFoundException(Medicamento.class,"O medicmaneto não foi encontrado."));

        String json = mapper.writeValueAsString(medicamento);

        mockMvc.perform(MockMvcRequestBuilders.put("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarDeletarMedicamento() throws Exception {

        Optional<Medicamento> medicamentoOptional = Optional.of(medicamento);

        Mockito.when(medicamentoService.buscarMedicamentoPorId(Mockito.anyInt())).thenReturn(medicamentoOptional);
        Mockito.doNothing().when(medicamentoService).deletarMedicamento(Mockito.any(Medicamento.class));

        String json = mapper.writeValueAsString(medicamento);

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.equalTo(1)));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarNaoEncontrarDeletarInvestimento() throws Exception {

        Optional<Medicamento> medicamentoOptional = Optional.empty();

        Mockito.when(medicamentoService.buscarMedicamentoPorId(Mockito.anyInt())).thenReturn(medicamentoOptional);
        Mockito.doNothing().when(medicamentoService).deletarMedicamento(Mockito.any(Medicamento.class));

        String json = mapper.writeValueAsString(medicamento);

        mockMvc.perform(MockMvcRequestBuilders.delete("/medicamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
    }


}

