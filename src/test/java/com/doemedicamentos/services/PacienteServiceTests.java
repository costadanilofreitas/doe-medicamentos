package com.doemedicamentos.services;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.repositories.EnderecoRepository;
import com.doemedicamentos.repositories.PacienteRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
public class PacienteServiceTests {

    @MockBean
    PacienteRepository pacienteRepository;

    @MockBean
    EnderecoRepository enderecoRepository;

    @Autowired
    PacienteService pacienteService;

    @Autowired
    EnderecoService enderecoService;

    Paciente paciente;

    Endereco endereco;

    @BeforeEach
    public void iniciar() throws ParseException {
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
    public void testarIncluirPaciente() {

        Mockito.when(pacienteRepository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

        Paciente pacienteObject = pacienteService.incluirPaciente(paciente);

        Assertions.assertEquals(paciente,pacienteObject);
    }

    @Test
    public void testarBuscarPacientePorId(){

        Optional<Paciente> pacienteOptional = Optional.of(paciente);

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);

        Optional<Paciente> pacienteRetorno = pacienteService.buscarPacientePorId(paciente.getIdPaciente());

        Assertions.assertEquals(pacienteRetorno.get(),paciente);
    }

    @Test
    public void testarBuscarPacientePorIdInexistente(){

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenThrow(new ObjectNotFoundException(Paciente.class, "Paciente não encontrado."));

        Assertions.assertThrows(ObjectNotFoundException.class, () -> { pacienteService.buscarPacientePorId(paciente.getIdPaciente());});
    }

    @Test
    public void testarAlterarPaciente(){

        Optional<Paciente> pacienteOptional = Optional.of(paciente);

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);

        paciente.setNome("Teste Nome Alterar");

        Mockito.when(pacienteRepository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

        Paciente pacienteAlterado = pacienteService.alterarPaciente(paciente);

        Assertions.assertEquals(paciente.getNome(), pacienteAlterado.getNome());
    }

    @Test
    public void testarAlterarPacienteInexistente(){

        Optional<Paciente> pacienteOptional = Optional.empty();;

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);

        paciente.setNome("Teste Nome Alterar");

        Mockito.when(pacienteRepository.save(Mockito.any(Paciente.class))).thenReturn(paciente);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> { pacienteService.alterarPaciente(paciente);});
    }

    @Test
    public void testarDeletar(){

        Optional<Paciente> pacienteOptional = Optional.of(paciente);

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);

        pacienteService.excluirPaciente(paciente);
        Mockito.verify(pacienteRepository, Mockito.times(1)).delete(Mockito.any(Paciente.class));
    }

    @Test
    public void testarDeletarInexistente(){

        Optional<Paciente> pacienteOptional = Optional.empty();

        Mockito.when(pacienteRepository.findById(Mockito.anyInt())).thenReturn(pacienteOptional);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> { pacienteService.excluirPaciente(paciente);});
    }

    @Test
    public void testarVincularEnderecoPorId(){

        Optional<Endereco> enderecoOptional = Optional.of(paciente.getEndereco());

        Mockito.when(enderecoService.buscarPorid(Mockito.anyInt())).thenReturn(enderecoOptional);

        Endereco enderecoObject = pacienteService.vincularEndereco(paciente.getEndereco());

        Assertions.assertEquals(enderecoObject.getEstado(), paciente.getEndereco().getEstado());
    }

    @Test
    public void testarVincularEnderecoPorEnderecoNovo(){

        Mockito.when(enderecoService.incluirEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        Endereco enderecoNovo = endereco;
        enderecoNovo.setIdEndereco(null);

        Endereco enderecoObject = pacienteService.vincularEndereco(enderecoNovo);

        Assertions.assertEquals(enderecoObject.getEstado(), enderecoNovo.getEstado());
    }

    @Test
    public void testarVincularEnderecoSemEndereco(){

        Endereco enderecoVazio = new Endereco();

        Mockito.when(enderecoService.incluirEndereco(Mockito.any(Endereco.class))).thenReturn(enderecoVazio);

        Endereco enderecoObject = pacienteService.vincularEndereco(enderecoVazio);

        Assertions.assertEquals(enderecoObject.getEstado(), enderecoVazio.getEstado());
    }
}
