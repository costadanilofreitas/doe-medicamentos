package com.doemedicamentos.models;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Endereco {

    private static final String NUMERO_NAO_PODE_ESTAR_VAZIO = "o numero não pode estar Vazio";
    private static final String ENDERECO_NAO_PODE_ESTAR_VAZIO = "o endereco não pode estar Vazio" ;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEndereco;

    @NotNull
    @NotEmpty(message = ENDERECO_NAO_PODE_ESTAR_VAZIO)
    private String endereco;

    @NotNull
    @NotEmpty(message = NUMERO_NAO_PODE_ESTAR_VAZIO)
    private String numero;

    @NotNull
    private String complemento;

    @NotNull
    @NotEmpty
    private String cidade;

    @NotNull
    @NotEmpty
    private String estado;

    public Endereco() {
    }

    public Endereco(int idEndereco, String endereco, String numero, String complemento, String cidade, String estado) {
        this.idEndereco = idEndereco;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.cidade = cidade;
        this.estado = estado;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
