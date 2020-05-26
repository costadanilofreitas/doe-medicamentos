package com.doemedicamentos.services;

import com.doemedicamentos.enums.StatusReserva;
import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.models.Reserva;
import com.doemedicamentos.repositories.ReservaRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class ReservaServiceTests {
    @MockBean
    ReservaRepository reservaRepository;

    @Autowired
    ReservaService reservaService;

    Reserva reserva;
    Doacao doacao;
    Medicamento medicamento;

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
        reserva = new Reserva("M45673", date, StatusReserva.RESERVADO, dataCadastro, doacao);
    }

    @Test
    public void testarIncluirReserva(){
        Mockito.when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(reserva);
        Reserva reservaObjeto = reservaService.incluirReserva(reserva);
        Assertions.assertEquals(reserva, reservaObjeto);
    }

    @Test
    public void testarIncluirReserva2(){
        Medicamento medicamentoObjeto = new Medicamento();
        medicamentoObjeto = medicamento;
        medicamentoObjeto.setControlado(false);
        Doacao doacaoObjeto = new Doacao();
        doacaoObjeto = doacao;
        doacaoObjeto.setMedicamento(medicamentoObjeto);
        reserva.setDoacao(doacaoObjeto);
        Mockito.when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(reserva);
        Reserva reservaObjeto = reservaService.incluirReserva(reserva);
        Assertions.assertEquals(reserva, reservaObjeto);
    }

    @Test
    public void testarIncluirReservaErro(){
        Reserva reserva1 = new Reserva();
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.RESERVADO);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {reservaService.incluirReserva(reserva1);});
    }

    @Test
    public void  testarBuscarReservaPorId(){
        reserva.setIdReserva(1);
        Optional<Reserva> retorno = Optional.of(reserva);
        Mockito.when(reservaRepository.findById(Mockito.anyInt())).thenReturn(retorno);
        Optional<Reserva> recervaObjeto = reservaService.buscarReservaPorId(1);
        Assertions.assertEquals(retorno, recervaObjeto);
    }

    @Test
    public void testarAlterarReserva()throws ParseException{
        reserva.setIdReserva(1);
        Reserva reservaAtualizado = new Reserva();
        reservaAtualizado.setIdReserva(1);
        Reserva reservaComAlteracao = new Reserva();
        reservaComAlteracao = reserva;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("05/15/20");
        reservaComAlteracao.setDataReceita(date);
        Mockito.when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(reservaComAlteracao);
        Reserva doacaoObjeto = reservaService.alterarReserva(reservaComAlteracao);
        Assertions.assertEquals(reservaComAlteracao.getDataReceita(), doacaoObjeto.getDataReceita());
    }

    @Test
    public void testarAlterarReservaError()throws ParseException{
        Reserva reserva1 = new Reserva();
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.FINALIZADA);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {reservaService.alterarReserva(reserva1);});
    }

    @Test
    public void testarDeletarReserva(){
        reserva.setIdReserva(1);
        reservaService.excluirSolicitacao(reserva);
        Mockito.verify(reservaRepository, Mockito.times(1)).delete(Mockito.any(Reserva.class));
    }

    @Test
    public void testarFinalizarReserva(){
        Reserva reserva1 = new Reserva();
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.FINALIZADA);
        Mockito.when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(reserva1);
        Reserva reservaObjeto = reservaService.finalizarReserva(reserva1);
        Assertions.assertEquals(reservaObjeto, reserva1);
    }

    @Test
    public void testarFinalizarReserva2()throws ParseException{
        Reserva reserva1 = new Reserva();
        reserva1.setDoacao(doacao);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("05/20/20");
        reserva1.setData_baixa(date);
        reserva1.setStatus(StatusReserva.FINALIZADA);
        Mockito.when(reservaRepository.save(Mockito.any(Reserva.class))).thenReturn(reserva1);
        Reserva reservaObjeto = reservaService.finalizarReserva(reserva1);
        Assertions.assertEquals(reservaObjeto, reserva1);
    }

    @Test
    public void ttestarFinalizarReservaError()throws ParseException{
        Reserva reserva1 = new Reserva();
        reserva1.setDoacao(doacao);
        reserva1.setStatus(StatusReserva.RESERVADO);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {reservaService.finalizarReserva(reserva1);});
    }
}
