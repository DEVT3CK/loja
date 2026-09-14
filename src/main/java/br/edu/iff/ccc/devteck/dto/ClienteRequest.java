package br.edu.iff.ccc.devteck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de entrada para cadastro/atualização de um cliente")
public class ClienteRequest {

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 150, message = "O nome deve ter entre 3 e 150 caracteres")
    @Schema(description = "Nome completo do cliente", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Informe um email válido")
    @Schema(description = "Email do cliente (usado no login)", example = "joao@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres")
    @Schema(description = "Senha de acesso do cliente", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;

    @NotBlank(message = "O telefone é obrigatório")
    @Schema(description = "Telefone de contato", example = "22999999999", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefone;

    @NotBlank(message = "O endereço é obrigatório")
    @Schema(description = "Endereço (logradouro e número)", example = "Rua das Flores, 123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String endereco;

    @NotBlank(message = "O CEP é obrigatório")
    @Size(min = 8, max = 9, message = "Informe um CEP válido")
    @Schema(description = "CEP", example = "28000-000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cep;

    @NotBlank(message = "A cidade é obrigatória")
    @Schema(description = "Cidade", example = "Campos dos Goytacazes", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    @Size(min = 2, max = 2, message = "Informe a sigla do estado (ex: RJ)")
    @Schema(description = "Sigla do estado (UF)", example = "RJ", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estado;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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
