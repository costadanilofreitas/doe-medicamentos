package com.doemedicamentos.models;

import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull(message = "Nome do medicamento deve ser preenchido.")
    @Column(name = "nome_medicamento")
    private String nome;
    @NotNull(message = "Nome do laboratorio deve ser preenchido.")
    @Column(name = "nome_laboratorio")
    private String laboratorio;
    @NotNull(message = "Remedio é controlado? TRUE ou FALSE.")
    private boolean controlado;

    public Medicamento() {
    }

    public Medicamento(int id, @NotNull(message = "Nome do medicamento deve ser preenchido.") String nome, @NotNull(message = "Nome do laboratorio deve ser preenchido.") String laboratorio, @NotNull(message = "Remedio é controlado? TRUE ou FALSE.") boolean controlado) {
        this.id = id;
        this.nome = nome;
        this.laboratorio = laboratorio;
        this.controlado = controlado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public boolean isControlado() {
        return controlado;
    }

    public void setControlado(boolean controlado) {
        this.controlado = controlado;
    }
}