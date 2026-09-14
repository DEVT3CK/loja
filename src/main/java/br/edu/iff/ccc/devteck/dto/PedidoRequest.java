package br.edu.iff.ccc.devteck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados necessários para finalizar um pedido a partir do carrinho do cliente")
public class PedidoRequest {

    @NotNull(message = "O id do cliente é obrigatório")
    @Positive(message = "O id do cliente deve ser um valor positivo")
    @Schema(description = "Id do cliente que está finalizando o pedido", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clienteId;

    @NotBlank(message = "O endereço de entrega é obrigatório")
    @Schema(description = "Endereço para entrega do pedido", example = "Rua das Flores, 123 - Campos dos Goytacazes/RJ",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String enderecoEntrega;

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

}
