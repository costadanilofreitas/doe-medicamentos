package com.doemedicamentos.repositories;

import com.doemedicamentos.models.Medicamento;
import org.springframework.data.repository.CrudRepository;

public interface MedicamentoRepository extends CrudRepository<Medicamento, Integer> {
}
