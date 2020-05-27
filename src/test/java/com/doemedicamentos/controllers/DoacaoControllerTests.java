package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.services.DoacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest(DoacaoController.class)
public class DoacaoControllerTests {
    @MockBean
    DoacaoService doacaoService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    Doacao doacao;
    Medicamento medicamento;

    @BeforeEach
    public void inicializar() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("09/29/20");
        Date dataCadastro = new Date();
        doacao = new Doacao();
        doacao.setDataValidade(date);
        doacao.setDataCadastro(dataCadastro);
        medicamento = new Medicamento();
        medicamento.setId(1);
        medicamento.setNome("Tecfidera");
        medicamento.setControlado(true);
        medicamento.setLaboratorio("Biogen Brasil Produtos");
        doacao.setMedicamento(medicamento);
    }

    @Test
    public void testarBuscarDoacaoPorId() throws Exception {
        doacao.setIdDocacao(1);
        Optional<Doacao> doacaoOptional = Optional.of(doacao);

        Mockito.when(doacaoService.buscarDoacaoPorId(1)).thenReturn(doacaoOptional);
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao/1").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocacao", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarBuscarDoacaoPorIdErro() throws Exception {
        doacao.setIdDocacao(1);
        Optional<Doacao> doacaoOptional = Optional.of(doacao);

        Mockito.when(doacaoService.buscarDoacaoPorId(1)).thenReturn(doacaoOptional);
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao/2").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarBuscarTodasDoacoes() throws Exception {
        Iterable<Doacao> listDocao = Arrays.asList();
        Mockito.when(doacaoService.buscarTodasDoacoes()).thenReturn(listDocao);
        String json = mapper.writeValueAsString(listDocao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void testarBuscarTodasDoacoesError() throws Exception {
        Iterable<Doacao> listDocao = Arrays.asList();
        Mockito.when(doacaoService.buscarTodasDoacoes()).thenThrow(new RuntimeException());
        String json = mapper.writeValueAsString(listDocao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testarBuscarDoacaoPorMedicamentoId() throws Exception {
        doacao.setIdDocacao(1);
        List<Doacao> listDocao = Arrays.asList(doacao);
        Mockito.when(doacaoService.buscarDoacaoPorMedicamento(Mockito.anyInt())).thenReturn(listDocao);
        String json = mapper.writeValueAsString(listDocao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao/buscarDoacaoPorMedicamento/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].idDocacao", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarBuscarDoacaoPorMedicamentoIdErro() throws Exception {
        doacao.setIdDocacao(1);
        List<Doacao> listDocao = Arrays.asList();
        Mockito.when(doacaoService.buscarDoacaoPorMedicamento(Mockito.anyInt())).thenReturn(listDocao);;
        String json = mapper.writeValueAsString(listDocao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao/buscarDoacaoPorMedicamento/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testarCriarDoacao() throws Exception {
        doacao.setIdDocacao(1);
        Mockito.when(doacaoService.incluirDoacao(Mockito.any(Doacao.class))).thenReturn(doacao);
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.post("/doacao").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocacao", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarAtualizarDoacao() throws Exception {
        doacao.setIdDocacao(1);
        Optional<Doacao> retorno = Optional.of(doacao);
        Doacao doacaoComAlteracao = new Doacao();
        doacaoComAlteracao = doacao;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date) formatter.parse("09/28/20");
        doacaoComAlteracao.setDataValidade(date);
        Mockito.when(doacaoService.alterarDoacao(Mockito.any(Doacao.class))).thenReturn(doacao);

        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.put("/doacao/1")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocacao", CoreMatchers.equalTo(1)));
    }

    @Test
    public void testarDeletarDoacao() throws Exception {
        doacao.setIdDocacao(1);
        Mockito.when(doacaoService.buscarDoacaoPorId(Mockito.anyInt())).thenReturn(Optional.of(doacao));
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.delete("/doacao/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocacao", CoreMatchers.equalTo(1)));

        Mockito.verify(doacaoService, Mockito.times(1)).excluirDoacao(doacao);
    }

    @Test
    public void testarDeletarLeadError() throws Exception {
        doacao.setIdDocacao(1);
        Mockito.when(doacaoService.buscarDoacaoPorId(Mockito.anyInt())).thenReturn(Optional.of(doacao));
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.delete("/doacao/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(doacaoService, Mockito.times(1)).excluirDoacao(doacao);
    }
}