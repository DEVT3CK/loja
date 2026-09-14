package br.edu.iff.ccc.devteck.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Novo status a ser atribuído a um pedido")
public class StatusRequest {

    @NotBlank(message = "O status é obrigatório")
    @Schema(description = "Novo status do pedido", example = "ENVIADO",
            allowableValues = {"PENDENTE", "PAGO", "ENVIADO", "ENTREGUE", "CANCELADO"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
