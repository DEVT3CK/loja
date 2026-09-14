package br.edu.iff.ccc.devteck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para adicionar (ou atualizar a quantidade de) um item no carrinho")
public class ItemCarrinhoRequest {

    @NotNull(message = "O id do produto é obrigatório")
    @Positive(message = "O id do produto deve ser positivo")
    @Schema(description = "Id do produto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    @Schema(description = "Quantidade desejada do produto", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer quantidade;

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

}
