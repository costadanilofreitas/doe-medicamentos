package com.doemedicamentos.services;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.repositories.DoacaoRepository;
import com.doemedicamentos.repositories.PacienteRepository;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DoacaoServiceTests {
    @MockBean
    DoacaoRepository doacaoRepository;

    @MockBean
    PacienteRepository pacienteRepository;

    @Autowired
    DoacaoService doacaoService;

    Doacao doacao;
    Medicamento medicamento;
    Paciente paciente;

    @BeforeEach
    public void inicializar() throws ParseException{
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
    public void testarIncluirDoacao(){
        Mockito.when(doacaoRepository.save(Mockito.any(Doacao.class))).thenReturn(doacao);
        Doacao doacaoObjeto = doacaoService.incluirDoacao(doacao);
        Assertions.assertEquals(doacao, doacaoObjeto);
    }

    @Test
    public void testarIncluirDoacaoErro(){
        Date date = new Date();
        doacao.setDataValidade(date);
        Mockito.when(doacaoRepository.save(Mockito.any(Doacao.class))).thenReturn(doacao);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> {doacaoService.incluirDoacao(doacao);});
    }

    @Test
    public void  testarBuscarDoacaoPorId(){
        doacao.setIdDocacao(1);
        Optional<Doacao> retorno = Optional.of(doacao);
        Mockito.when(doacaoRepository.findById(Mockito.anyInt())).thenReturn(retorno);
        Optional<Doacao> doacaoObjeto = doacaoService.buscarDoacaoPorId(1);
        Assertions.assertEquals(retorno, doacaoObjeto);
    }

    @Test
    public void testarBuscarTodasDoacoes(){
        doacao.setIdDocacao(1);
        Mockito.when(doacaoRepository.findAll()).thenReturn(Arrays.asList(doacao));
        Iterable<Doacao> doacaoIterable = doacaoService.buscarTodasDoacoes();
        List<Doacao> retorno = (List)doacaoIterable;
        Assertions.assertEquals(1, retorno.size());
        Assertions.assertEquals(1, retorno.get(0).getIdDocacao());
    }

    @Test
    public void testarAlterarDoacao()throws ParseException{
        doacao.setIdDocacao(1);
        Doacao doacaoAtualizado = new Doacao();
        doacaoAtualizado.setIdDocacao(1);
        Doacao doacaoComAlteracao = new Doacao();
        doacaoComAlteracao = doacao;
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("09/28/20");
        doacaoComAlteracao.setDataValidade(date);
        Optional<Doacao> retorno = Optional.of(doacao);
        Mockito.when(doacaoRepository.findById(Mockito.anyInt())).thenReturn(retorno);
        Mockito.when(doacaoRepository.save(Mockito.any(Doacao.class))).thenReturn(doacaoComAlteracao);
        Doacao doacaoObjeto = doacaoService.alterarDoacao(doacaoComAlteracao);
        Assertions.assertEquals(doacaoComAlteracao.getDataValidade(), doacaoObjeto.getDataValidade());
    }

    @Test
    public void testarAlterarDoacaoInexistente()throws Exception{

        Optional<Doacao> retornoEmpty = Optional.empty();
        Mockito.when(doacaoRepository.findById(Mockito.anyInt())).thenReturn(retornoEmpty);
        Mockito.when(doacaoService.buscarDoacaoPorId(Mockito.anyInt())).thenReturn(retornoEmpty);
        DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
        Date date = (Date)formatter.parse("09/28/20");
        doacao.setDataCadastro(date);
        doacao.setIdDocacao(10);
        Mockito.when(doacaoRepository.save(Mockito.any(Doacao.class))).thenReturn(doacao);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> { doacaoService.alterarDoacao(doacao);});
    }

    @Test
    public void testarDeletarDoacao(){
        doacao.setIdDocacao(1);
        doacaoService.excluirDoacao(doacao);
        Mockito.verify(doacaoRepository, Mockito.times(1)).delete(Mockito.any(Doacao.class));
    }

    @Test
    public void testarDeletarDoacaoInexistente(){
        Optional<Doacao> retornoEmpty = Optional.empty();
        Mockito.when(doacaoRepository.findById(Mockito.anyInt())).thenReturn(retornoEmpty);
        Mockito.when(doacaoService.buscarDoacaoPorId(Mockito.anyInt())).thenReturn(retornoEmpty);
        doacao.setIdDocacao(2);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> { doacaoService.excluirDoacao(doacao);});
    }

    @Test
    public void testarBuscarDoacaoPorMedicamento(){
        Mockito.when(doacaoRepository.findAll()).thenReturn(Arrays.asList(doacao));
        List<Doacao> doacaos = doacaoService.buscarDoacaoPorMedicamento(1);
        Assertions.assertEquals(1, doacaos.size());
        Assertions.assertEquals("Tecfidera", doacaos.get(0).getMedicamento().getNome());

    }

    @Test
    public void testarBuscarPacientePorId(){

        Optional<Paciente> pacienteOptional = Optional.of(paciente);
        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);
        Paciente pacienteBusca = doacaoService.buscarPacientePorId(1);
        Assertions.assertEquals(paciente.getNome(),pacienteBusca.getNome());
    }
}
