package com.doemedicamentos.repositories;

import com.doemedicamentos.models.Doacao;
import org.springframework.data.repository.CrudRepository;

public interface DoacaoRepository extends CrudRepository<Doacao, Integer> {
}
