package com.doemedicamentos.services;

import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.repositories.MedicamentoRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MedicamentoServiceTests {

    @MockBean
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private MedicamentoService medicamentoService;

    Medicamento medicamento;

    @BeforeEach
    public void inicializar(){
        medicamento = new Medicamento();
        medicamento.setId(1);
        medicamento.setNome("Aspirina");
        medicamento.getLaboratorio("Bayer");
        medicamento.isControlado(false);
    }

    @Test
    public void testarbuscarTodosMedicamentos(){

        Iterable<Medicamento> medicamentoIterable = Arrays.asList(medicamento);

        Mockito.when(medicamentoRepository.findAll()).thenReturn(medicamentoIterable);

        Iterable<Medicamento> medicamentoObj = medicamentoService.buscarTodosMedicamentos();

        Assertions.assertEquals(medicamentoIterable,medicamentoObj);
    }

    @Test
    public void testarBuscarMedicamentoPorId(){

        Optional<Medicamento> medicamentoOptional = Optional.of(medicamento);
        Mockito.when(medicamentoRepository.findById(Mockito.any(int.class))).thenReturn(medicamentoOptional);

        int id=1;
        Optional<Medicamento> medicamentoOptionalObj = medicamentoService.buscarMedicamentoPorId(id);

        Assertions.assertEquals(medicamentoOptionalObj, medicamentoOptional);
        Mockito.verify(medicamentoRepository, Mockito.times(1)).findById(Mockito.any(int.class));
    }

    @Test
    public void testarIncluirMedicamento(){

        Mockito.when(medicamentoRepository.save( Mockito.any(Medicamento.class))).thenReturn(medicamento);
        Medicamento medicamentoObj = medicamentoService.incluirMedicamento(medicamento);
        Assertions.assertEquals(medicamento, medicamentoObj);
    }

    @Test
    public void testarAtualizarMedicamento(){

        Optional<Medicamento> medicamentoOptional = Optional.of(medicamento);
        Mockito.when(medicamentoRepository.findById(Mockito.anyInt())).thenReturn(medicamentoOptional);
        Mockito.when(medicamentoRepository.save(Mockito.any(Medicamento.class))).thenReturn(medicamento);

        Medicamento medicamentoObj = medicamentoService.atualizarMedicamento(medicamento);

        Assertions.assertEquals(medicamentoObj, medicamento);
    }

    @Test
    public void testarNaoEncontrouAtualizarMedicamento(){

        Medicamento medicamento1 = new Medicamento();
        medicamento1.setId(2);
        medicamento1.setNome("Polaramine");
        medicamento1.getLaboratorio("Roche");
        medicamento1.isControlado(false);

        Optional<Medicamento> medicamentoOptional = Optional.empty();
        Mockito.when(medicamentoRepository.findById(Mockito.anyInt())).thenReturn(medicamentoOptional);
        Mockito.when(medicamentoRepository.save(Mockito.any(Medicamento .class))).thenReturn(medicamento1);

        Assertions.assertThrows(ObjectNotFoundException.class,() -> {medicamentoService.atualizarMedicamento(medicamento);});
    }

    @Test
    public void testarDeletarInvestimento(){
        medicamentoService.deletarMedicamento(medicamento);
        Mockito.verify(medicamentoRepository, Mockito.times(1)).delete(Mockito.any(Medicamento.class));
    }

}
