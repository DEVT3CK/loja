package br.edu.iff.ccc.devteck.controller.apirest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Status", description = "Verificação de disponibilidade da API")
public class ApiRestController {

    @GetMapping("/status")
    @Operation(summary = "Verifica se a API está no ar")
    public String status() {
        return "API DevTeck v1 - Produtos, Clientes, Pedidos e Carrinho disponíveis";
    }

}
