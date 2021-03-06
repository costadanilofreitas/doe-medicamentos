package com.doemedicamentos.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class Doacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDocacao;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Data de vencimento do medicamento deve ser preenchida.")
    private Date dataValidade;
    @JsonFormat(pattern="yyyy-MM-dd")
    @NotNull(message = "Data do cadastro do medicamento deve ser preenchida.")
    private Date dataCadastro;

    @OneToOne
    private Medicamento medicamento;

    @OneToOne
    private Paciente paciente;

    public Doacao() {
    }

    public Doacao(Integer idDocacao, Date dataValidade, Date dataCadastro) {
        this.idDocacao = idDocacao;
        this.dataValidade = dataValidade;
        this.dataCadastro = dataCadastro;
    }

    public Integer getIdDocacao() {
        return idDocacao;
    }

    public void setIdDocacao(Integer idDocacao) {
        this.idDocacao = idDocacao;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}
