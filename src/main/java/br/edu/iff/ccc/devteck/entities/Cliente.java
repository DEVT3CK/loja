package br.edu.iff.ccc.devteck.entities;

import java.time.LocalDate;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
    private String telefone;
    private String endereco;
    private String cep;
    private String cidade;
    private String estado;

    public Cliente() {
        super();
    }

    public Cliente(Long id, String nome, String email, String senha, LocalDate dataCadastro,
                   String telefone, String endereco, String cep, String cidade, String estado) {
        super(id, nome, email, senha, dataCadastro);
        this.telefone = telefone;
        this.endereco = endereco;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
    }

    //Get e Set
    @Override
    public String getTipo() {
        return "CLIENTE";
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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
