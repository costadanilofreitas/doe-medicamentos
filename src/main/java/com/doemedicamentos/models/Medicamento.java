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

    public String getLaboratorio(String bayer) {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public boolean isControlado(boolean b) {
        return controlado;
    }

    public void setControlado(boolean controlado) {
        this.controlado = controlado;
    }

}