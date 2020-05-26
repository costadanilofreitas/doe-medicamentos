package com.doemedicamentos.repositories;

import com.doemedicamentos.models.Paciente;
import org.springframework.data.repository.CrudRepository;

public interface PacienteRepository extends CrudRepository<Paciente, Integer> {
}
