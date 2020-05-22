package com.doemedicamentos.repositories;

import com.doemedicamentos.models.Endereco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends CrudRepository<Endereco, Integer> {

    Iterable<Endereco> findByEstado(String estado);
    Iterable<Endereco> findByCidade(String cidade);
    Iterable<Endereco> findByEstadoAndCidade(String estado, String cidade);

}
