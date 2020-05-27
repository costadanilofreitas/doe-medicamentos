package com.doemedicamentos.controllers;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.DoacaoService;
import com.doemedicamentos.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@WebMvcTest(DoacaoController.class)
@Import(JWTUtil.class)
public class DoacaoControllerTests {
    @MockBean
    DoacaoService doacaoService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    Doacao doacao;
    Medicamento medicamento;
    Paciente paciente;

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
        paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setDataNascimento(new SimpleDateFormat( "yyyyMMdd" ).parse( "20100520" ));
        paciente.setEmail("teste@gmail.com");
        paciente.setNome("Nome Paciente");
        paciente.setTelefone("11999999999");
        doacao.setPaciente(paciente);
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarBuscarDoacaoPorIdErro() throws Exception {
        doacao.setIdDocacao(1);
        Optional<Doacao> doacaoOptional = Optional.of(doacao);

        Mockito.when(doacaoService.buscarDoacaoPorId(1)).thenReturn(doacaoOptional);
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.get("/doacao/2").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarCriarDoacao() throws Exception {

        Mockito.when(doacaoService.buscarPacientePorId(Mockito.anyInt())).thenReturn(paciente);
        doacao.setIdDocacao(1);
        Mockito.when(doacaoService.incluirDoacao(Mockito.any(Doacao.class))).thenReturn(doacao);
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.post("/doacao").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idDocacao", CoreMatchers.equalTo(1)));
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAtualizarDoacao() throws Exception {
        Mockito.when(doacaoService.buscarPacientePorId(Mockito.anyInt())).thenReturn(paciente);
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarAtualizarDoacaoInexistente() throws Exception {
        Mockito.when(doacaoService.buscarPacientePorId(Mockito.anyInt())).thenReturn(paciente);
        doacao.setIdDocacao(1);
        Optional<Doacao> retorno = Optional.of(doacao);
        Doacao doacaoComAlteracao = new Doacao();
        doacaoComAlteracao = doacao;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date) formatter.parse("09/28/20");
        doacaoComAlteracao.setDataValidade(date);
        doacaoComAlteracao.setIdDocacao(2);
        Mockito.when(doacaoService.alterarDoacao(Mockito.any(Doacao.class))).thenThrow(new ObjectNotFoundException(Paciente.class, "Doação não encontrada."));
        String json = mapper.writeValueAsString(doacaoComAlteracao);

        mockMvc.perform(MockMvcRequestBuilders.put("/doacao/2")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
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
    @WithMockUser(username = "usuario@gmail.com", password = "aviao11")
    public void testarDeletarLeadError() throws Exception {
        doacao.setIdDocacao(1);
        Mockito.when(doacaoService.buscarDoacaoPorId(Mockito.anyInt())).thenReturn(Optional.empty());
        String json = mapper.writeValueAsString(doacao);

        mockMvc.perform(MockMvcRequestBuilders.delete("/doacao/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}