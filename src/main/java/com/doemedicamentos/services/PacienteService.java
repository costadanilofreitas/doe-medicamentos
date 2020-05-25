package com.doemedicamentos.services;

import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.repositories.PacienteRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PacienteService {

    @Autowired
    PacienteRepository pacienteRepository;

    //@Autowired
    //EnderecoRepository enderecoRepository;

    public Paciente incluirPaciente(Paciente paciente){
        Paciente pacienteObjeto = pacienteRepository.save(paciente);
        return pacienteObjeto;
    }

    public Optional<Paciente> buscarPacientePorId(int idPaciente){
        Optional<Paciente> optionalPaciente = pacienteRepository.findById(idPaciente);

        if(optionalPaciente.isPresent()){
            return optionalPaciente;
        }
        else {
            throw new ObjectNotFoundException(Paciente.class, "Paciente não encontrado.");
        }
    }

    public Paciente alterarPaciente(Paciente paciente){

        try {
            Optional<Paciente> pacienteOptional = buscarPacientePorId(paciente.getIdPaciente());
            Paciente pacienteObject = pacienteRepository.save(paciente);
            return pacienteObject;
        }
        catch (Exception e){
            throw new ObjectNotFoundException(Paciente.class, e.getMessage());
        }
    }

    public void excluirPaciente(Paciente paciente){
        try {
            Optional<Paciente> pacienteOptional = buscarPacientePorId(paciente.getIdPaciente());
            pacienteRepository.delete(paciente);
        }
        catch (Exception e){
            throw new ObjectNotFoundException(Paciente.class, e.getMessage());
        }
    }

}
