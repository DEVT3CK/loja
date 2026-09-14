package br.edu.iff.ccc.devteck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados de entrada para cadastro/atualização de um produto")
public class ProdutoRequest {

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres")
    @Schema(description = "Nome do produto", example = "Arroz Branco Tipo 1 - 5kg", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    @Schema(description = "Descrição detalhada do produto", example = "Pacote de arroz branco tipo 1, 5kg")
    private String descricao;

    @PositiveOrZero(message = "O código de barras não pode ser negativo")
    @Schema(description = "Código de barras (EAN) do produto", example = "7891234567895")
    private long codigoBarras;

    @Positive(message = "O preço deve ser maior que zero")
    @Schema(description = "Preço unitário do produto", example = "24.90", requiredMode = Schema.RequiredMode.REQUIRED)
    private double preco;

    @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa")
    @Schema(description = "Quantidade disponível em estoque", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    private int quantidadeEstoque;

    @NotBlank(message = "A categoria é obrigatória")
    @Schema(description = "Categoria do produto", example = "Mercearia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String categoria;

    @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres")
    @Schema(description = "URL da imagem do produto", example = "https://cdn.devteck.com/produtos/arroz.jpg")
    private String imagem;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(long codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

}
