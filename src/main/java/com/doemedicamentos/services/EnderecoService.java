package com.doemedicamentos.services;

import com.doemedicamentos.models.Endereco;
import com.doemedicamentos.repositories.EnderecoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoService {

    private static final String ENDERECO_NAO_ENCONTRADO = "Endereço não encontrado";

    @Autowired
    EnderecoRepository enderecoRepository;


    public Optional<Endereco> buscarPorid(Integer id){
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);
        return enderecoOptional;
    }

    public Iterable<Endereco> buscarPorEstadoECidade(String estado , String cidade){
        Iterable<Endereco> enderecos = enderecoRepository.findByEstadoAndCidade(estado,cidade);
        return enderecos;
    }

    public Iterable<Endereco> buscarPorEstado(String estado){
        Iterable<Endereco> enderecos = enderecoRepository.findByEstado(estado);
        return enderecos;
    }
    public Iterable<Endereco> buscarPorCidade(String cidade){
        Iterable<Endereco> enderecos = enderecoRepository.findByCidade(cidade);
        return enderecos;
    }


    public Iterable<Endereco> buscarEnderecos(){
        Iterable<Endereco> enderecos = enderecoRepository.findAll();
        return enderecos;
    }

    public Endereco incluirEndereco(Endereco endereco){
        Endereco enderecoObjeto = enderecoRepository.save(endereco);
        return enderecoObjeto;
    }

    public Endereco alterarEndereco(Endereco endereco) throws ObjectNotFoundException {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(endereco.getIdEndereco());
        if (enderecoOptional.isPresent()) {
            Endereco enderecoObjeto = enderecoRepository.save(endereco);
            return enderecoObjeto;
        }else{
             throw new ObjectNotFoundException(Endereco.class, ENDERECO_NAO_ENCONTRADO);
        }
     }

     public Endereco excluirEndereco (Endereco endereco) throws ObjectNotFoundException{
         Optional<Endereco> enderecoOptional = enderecoRepository.findById(endereco.getIdEndereco());
         if (enderecoOptional.isPresent()) {
             enderecoRepository.delete(endereco);
             return endereco;
         }else{
             throw new ObjectNotFoundException(Endereco.class, ENDERECO_NAO_ENCONTRADO);
         }
     }
}
