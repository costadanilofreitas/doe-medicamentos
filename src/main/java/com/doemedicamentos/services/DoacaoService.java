package com.doemedicamentos.services;

import com.doemedicamentos.models.Doacao;
import com.doemedicamentos.models.Paciente;
import com.doemedicamentos.repositories.DoacaoRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoacaoService {

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private PacienteService pacienteService;

    public Paciente buscarPacientePorId(Integer id){

        Optional<Paciente> paciente = pacienteService.buscarPacientePorId(id);

        return paciente.get();
    }

    public Doacao incluirDoacao(Doacao doacao){
        Date date = new Date();
        if(doacao.getDataValidade().getTime() > date.getTime()){
            return doacaoRepository.save(doacao);
        }
        throw new ObjectNotFoundException("", "Não é permitido a doação de medicamento fora do prazo de validade! ");
    }

    public Optional<Doacao> buscarDoacaoPorId(int idDoacao){
        return doacaoRepository.findById(idDoacao);
    }

    public Iterable<Doacao> buscarTodasDoacoes(){
        return doacaoRepository.findAll();
    }

    public Doacao alterarDoacao(Doacao doacao){

        Optional<Doacao> optionalDoacao = buscarDoacaoPorId(doacao.getIdDocacao());

        if(optionalDoacao.isPresent()){
            return  doacaoRepository.save(doacao);
        }
        else {
            throw new ObjectNotFoundException(Paciente.class, "Doação não encontrada.");
        }
    }

    public void excluirDoacao(Doacao doacao){ doacaoRepository.delete(doacao);}

    public List<Doacao> buscarDoacaoPorMedicamento(int idMedicacao){
        Iterable<Doacao> doacaoAll = doacaoRepository.findAll();
        List<Doacao> doacoesMedicamento = new ArrayList<>();
        for(Doacao doacao : doacaoAll){
            if(doacao.getMedicamento().getId() == idMedicacao){
                doacoesMedicamento.add(doacao);
            }
        }
        return doacoesMedicamento;
    }
}
