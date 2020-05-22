package com.doemedicamentos.services;

import com.doemedicamentos.models.Medicamento;
import com.doemedicamentos.repositories.MedicamentoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Iterable<Medicamento> buscarTodosMedicamentos(){
        Iterable<Medicamento> medicamentoIterable = medicamentoRepository.findAll();
        return  medicamentoIterable;
    }

    public Optional<Medicamento> buscarMedicamentoPorId(int id){
        Optional<Medicamento> medicamentoOptional = medicamentoRepository.findById(id);
        return medicamentoOptional;
    }

    public Medicamento incluirMedicamento(Medicamento medicamento) {
        Medicamento medicamentoObj = medicamentoRepository.save(medicamento);
        return medicamentoObj;
    }

    public Medicamento atualizarMedicamento(Medicamento medicamento) {
        Optional<Medicamento> medicamentoOptional = buscarMedicamentoPorId(medicamento.getId());
        if (medicamentoOptional.isPresent()) {
            Medicamento medicamentoObj = medicamentoRepository.save(medicamento);
            return medicamentoObj;
        }else{
            throw new ObjectNotFoundException(Medicamento.class,"O medicamento não foi encontrado para atualização.");
        }
    }

    public void deletarMedicamento(Medicamento medicamento){
        medicamentoRepository.delete(medicamento);
    }


}
