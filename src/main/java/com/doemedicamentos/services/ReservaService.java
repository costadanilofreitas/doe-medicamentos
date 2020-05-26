package com.doemedicamentos.services;

import com.doemedicamentos.enums.StatusReserva;
import com.doemedicamentos.models.Reserva;
import com.doemedicamentos.repositories.ReservaRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    public Reserva incluirReserva(Reserva reserva){
        if(reserva.getDoacao().getMedicamento().isControlado() == true && reserva.getDataReceita() != null &&
            reserva.getCRM() != null && reserva.getStatus() != StatusReserva.FINALIZADA){
            return reservaRepository.save(reserva);
        }else if(reserva.getDoacao().getMedicamento().isControlado() == false && reserva.getStatus() != StatusReserva.FINALIZADA){
            return reservaRepository.save(reserva);
        }
        throw new ObjectNotFoundException("", "O remédio em questão é controloado, desta forma se faz necessário as informações do CRM do médio e a data da receita!" +
                " Verifique se a reserva não foi FINALIZADA");
    }

    public Optional<Reserva> buscarReservaPorId(int id){
        return reservaRepository.findById(id);
    }

    public Reserva alterarReserva(Reserva reserva){
        if(reserva.getDoacao().getMedicamento().isControlado() == true && reserva.getDataReceita() != null &&
                reserva.getCRM() != null && reserva.getStatus() != StatusReserva.FINALIZADA){
            return reservaRepository.save(reserva);
        }
        throw new ObjectNotFoundException("", "O remédio em questão é controloado, desta forma se faz necessário as informações do CRM do médio e a data da receita!" +
                " Verifique se a reserva não foi FINALIZADA");
    }

    public void excluirSolicitacao(Reserva reserva){
        reservaRepository.delete(reserva);
    }

    public Reserva finalizarReserva(Reserva reserva){
        if(reserva.getData_baixa() == null){
            Date date = new Date();
            reserva.setData_baixa(date);
        }
        if(reserva.getStatus() == StatusReserva.FINALIZADA){
            return reservaRepository.save(reserva);
        }
        throw new ObjectNotFoundException("", "Para finalizar a reserva o status esperado é FINALIZADA!");
    }

}
