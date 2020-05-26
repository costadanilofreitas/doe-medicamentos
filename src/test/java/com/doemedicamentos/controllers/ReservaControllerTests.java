package com.doemedicamentos.controllers;

import com.doemedicamentos.enums.StatusReserva;
import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.models.Reserva;
import com.doemedicamentos.security.JWTUtil;
import com.doemedicamentos.services.ReservaService;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@WebMvcTest(ReservaController.class)
@Import(JWTUtil.class)
public class ReservaControllerTests {
    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    ReservaService reservaService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    Reserva reserva;
    Doacao doacao;
    Medicamento medicamento;
    Paciente paciente;

    @BeforeEach
    public void inicializar()throws ParseException {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("09/29/20");
        Date dataCadastro = new Date();
        doacao = new Doacao(1, date, dataCadastro);
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
        reserva = new Reserva("M45673", date, StatusReserva.RESERVADO, dataCadastro, doacao, paciente);
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarCriarReserva() throws Exception {
        reserva.setIdReserva(1);
        Mockito.when(reservaService.incluirReserva(Mockito.any(Reserva.class))).thenReturn(reserva);
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.post("/reserva").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idReserva", CoreMatchers.equalTo(1)));

    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarCriarReservaError() throws Exception {
        Mockito.when(reservaService.incluirReserva(Mockito.any(Reserva.class))).thenThrow(new ObjectNotFoundException("", "O remédio em questão é controloado, desta forma se faz necessário as informações do CRM do médio e a data da receita!" +
                " Verifique se a reserva não foi FINALIZADA"));
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.post("/reserva").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarBuscarReservaPorId() throws Exception {
        reserva.setIdReserva(1);
        Optional<Reserva> reservaOptional = Optional.of(reserva);

        Mockito.when(reservaService.buscarReservaPorId(1)).thenReturn(reservaOptional);
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.get("/reserva/1").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idReserva", CoreMatchers.equalTo(1)));
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarBuscarReservaPorIdErro() throws Exception {
        reserva.setIdReserva(1);
        Optional<Reserva> reservaOptional = Optional.of(reserva);

        Mockito.when(reservaService.buscarReservaPorId(1)).thenReturn(reservaOptional);
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.get("/reserva/2").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarAlterarReserva() throws Exception {
        reserva.setIdReserva(1);
        Reserva reservaAtualizado = new Reserva();
        reservaAtualizado.setIdReserva(1);
        Reserva reservaComAlteracao = new Reserva();
        reservaComAlteracao = reserva;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("05/15/20");
        reservaComAlteracao.setDataReceita(date);
        Mockito.when(reservaService.alterarReserva(Mockito.any(Reserva.class))).thenReturn(reserva);

        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.put("/reserva/1")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idReserva", CoreMatchers.equalTo(1)));
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarAlterarReservaError() throws Exception {
        Mockito.when(reservaService.alterarReserva(Mockito.any(Reserva.class))).thenThrow(new ObjectNotFoundException("", "O remédio em questão é controloado, desta forma se faz necessário as informações do CRM do médio e a data da receita!" +
                " Verifique se a reserva não foi FINALIZADA"));
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.put("/reserva/1").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarExcluirReserva() throws Exception {
        reserva.setIdReserva(1);
        Mockito.when(reservaService.buscarReservaPorId(Mockito.anyInt())).thenReturn(Optional.of(reserva));
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reserva/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andExpect(MockMvcResultMatchers.jsonPath("$.idReserva", CoreMatchers.equalTo(1)));

        Mockito.verify(reservaService, Mockito.times(1)).excluirSolicitacao(reserva);
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarExcluirReservaError() throws Exception {
        reserva.setIdReserva(1);
        Mockito.when(reservaService.buscarReservaPorId(Mockito.anyInt())).thenReturn(Optional.of(reserva));
        String json = mapper.writeValueAsString(reserva);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reserva/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(reservaService, Mockito.times(1)).excluirSolicitacao(reserva);
    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarFinalizarReserva() throws Exception {
        Reserva reserva1 = new Reserva();
        reserva1.setIdReserva(1);
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.RESERVADO);
        Mockito.when(reservaService.finalizarReserva(Mockito.any(Reserva.class))).thenThrow(new ObjectNotFoundException("", "Para finalizar a reserva o status esperado é FINALIZADA!"));
        String json = mapper.writeValueAsString(reserva1);

        mockMvc.perform(MockMvcRequestBuilders.put("/reserva/finalizar/1").contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @WithMockUser(username = "marianafelix@gmail.com", password = "senha")
    public void testarFinalizarReservaErro() throws Exception {
        Reserva reserva1 = new Reserva();
        reserva1.setIdReserva(1);
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.FINALIZADA);
        Mockito.when(reservaService.finalizarReserva(Mockito.any(Reserva.class))).thenReturn(reserva1);
        String json = mapper.writeValueAsString(reserva1);

        mockMvc.perform(MockMvcRequestBuilders.put("/reserva/finalizar/1")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.equalTo("FINALIZADA")));

    }
}
