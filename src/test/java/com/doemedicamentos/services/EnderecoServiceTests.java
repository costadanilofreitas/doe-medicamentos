package com.doemedicamentos.services;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.repositories.EnderecoRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class EnderecoServiceTests {

    private static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado";

    @MockBean
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoService enderecoService;

    private Endereco endereco = new Endereco();
    private Endereco enderecoAlterado = new Endereco();

    @BeforeEach
    public void inicializar(){
    endereco.setIdEndereco(1);
    endereco.setNumero("100");
    endereco.setCidade("São Paulo");
    endereco.setComplemento("apartamento 142");
    endereco.setEstado("São Paulo");

    enderecoAlterado.setIdEndereco(1);
    enderecoAlterado.setNumero("110");
    enderecoAlterado.setCidade("São Paulo");
    enderecoAlterado.setComplemento("apartamento 142");
    enderecoAlterado.setEstado("São Paulo");



    }

    @Test public void testarBuscarPorid() throws Exception {
        Mockito.when(enderecoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(endereco));
        enderecoService.buscarPorid(endereco.getIdEndereco());
        Mockito.verify(enderecoRepository).findById(endereco.getIdEndereco());

    }

    @Test public void testarBuscarPorEstadoECidade() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoRepository.findByEstadoAndCidade(endereco.getEstado(), endereco.getCidade()))
                .thenReturn(enderecosIterable);
        Iterable<Endereco> enderecos = enderecoService.buscarPorEstadoECidade(endereco.getEstado(), endereco.getCidade());
        Assertions.assertEquals(enderecos, enderecosIterable);

    }
    @Test public void testarBuscarPorEstado() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoRepository.findByEstado(endereco.getEstado()))
                .thenReturn(enderecosIterable);
        Iterable<Endereco> enderecos = enderecoService.buscarPorEstado(endereco.getEstado());
        Assertions.assertEquals(enderecos, enderecosIterable);

    }

    @Test public void testarBuscarPorCidade() throws Exception {
        Iterable<Endereco> enderecosIterable = Arrays.asList(endereco);
        Mockito.when(enderecoRepository.findByCidade(endereco.getCidade()))
                .thenReturn(enderecosIterable);
        Iterable<Endereco> enderecos = enderecoService.buscarPorCidade(endereco.getCidade());
        Assertions.assertEquals(enderecos, enderecosIterable);

    }
    @Test public void testarBuscarEnderecos() throws Exception {
        Iterable<Endereco> enderecoIterable = Arrays.asList(endereco);
        Mockito.when(enderecoRepository.findAll())
                .thenReturn(enderecoIterable);
        Iterable<Endereco> enderecos = enderecoService.buscarEnderecos();
        Mockito.verify(enderecoRepository).findAll();
        Assertions.assertEquals(enderecos,enderecoIterable);

    }

    @Test public void testarIncluirEndereco() throws Exception {
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);
        Endereco enderecoObjeto = enderecoService.incluirEndereco(endereco);

        Assertions.assertEquals(endereco, enderecoObjeto);
        Assertions.assertEquals(endereco.getIdEndereco(),enderecoObjeto.getIdEndereco());
        Assertions.assertEquals(endereco.getCidade(),enderecoObjeto.getCidade());
        Assertions.assertEquals(endereco.getEstado(),enderecoObjeto.getEstado());
        Assertions.assertEquals(endereco.getComplemento(),enderecoObjeto.getComplemento());
        Assertions.assertEquals(endereco.getEndereco(),enderecoObjeto.getEndereco());
        Assertions.assertEquals(endereco.getNumero(),enderecoObjeto.getNumero());

    }

    @Test public void testarAlterarEnderecoNumero() throws Exception {
        Mockito.when(enderecoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(endereco));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);
        enderecoAlterado.setNumero("125");
        Endereco enderecoObjeto = enderecoService.alterarEndereco(enderecoAlterado);
        Assertions.assertEquals(endereco.getNumero(),enderecoObjeto.getNumero());

    }

    @Test public void testarAlterarEnderecoInexistente() throws Exception {
        Mockito.when(enderecoRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> enderecoService.alterarEndereco(enderecoAlterado),
                ENDERECO_NAO_ENCONTRADO);
    }

    @Test public void testarExcluirEndereco() throws Exception {
        Mockito.when(enderecoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(endereco));
        Mockito.when(enderecoRepository.save(Mockito.any(Endereco.class))).thenReturn(endereco);
        Endereco enderecoObjeto = enderecoService.excluirEndereco(endereco);
        Mockito.verify(enderecoRepository).delete(Mockito.any(Endereco.class));
    }

    @Test public void testarExcluirEnderecoInexistente() throws Exception {
        Mockito.when(enderecoRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        Assertions.assertThrows(ObjectNotFoundException.class,
                () -> enderecoService.excluirEndereco(endereco),
                ENDERECO_NAO_ENCONTRADO);
    }

}
