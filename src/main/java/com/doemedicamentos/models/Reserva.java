package com.doemedicamentos.models;

import com.doemedicamentos.enums.StatusReserva;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Reserva{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;
    private String CRM;
    private Date dataReceita;
    @NotNull(message = "Status deve estar preenchido.")
    private StatusReserva status;
    private Date data_baixa;

    @OneToOne
    @NotNull(message = "É necessário associar a reserva a uma doação.")
    private Doacao doacao;

    @OneToOne
    @NotNull(message = "É necessário associar a reserva a um paciente.")
    private Paciente paciente;

    public Reserva() {
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public String getCRM() {
        return CRM;
    }

    public void setCRM(String CRM) {
        this.CRM = CRM;
    }

    public Date getDataReceita() {
        return dataReceita;
    }

    public void setDataReceita(Date dataReceita) {
        this.dataReceita = dataReceita;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public void setStatus(StatusReserva status) {
        this.status = status;
    }

    public Date getData_baixa() {
        return data_baixa;
    }

    public void setData_baixa(Date data_baixa) {
        this.data_baixa = data_baixa;
    }

    public Doacao getDoacao() {
        return doacao;
    }

    public void setDoacao(Doacao doacao) {
        this.doacao = doacao;
    }
    public Reserva(String CRM, Date dataReceita, @NotNull(message = "Status deve estar preenchido.") StatusReserva status, Date data_baixa, @NotNull(message = "É necessário associar a reserva a uma doação.") Doacao doacao, @NotNull(message = "É necessário associar a reserva a um paciente.") Paciente paciente) {
        this.CRM = CRM;
        this.dataReceita = dataReceita;
        this.status = status;
        this.data_baixa = data_baixa;
        this.doacao = doacao;
        this.paciente = paciente;
    }
}

