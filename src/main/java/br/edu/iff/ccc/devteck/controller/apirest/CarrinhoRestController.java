package br.edu.iff.ccc.devteck.controller.apirest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iff.ccc.devteck.dto.ItemCarrinhoRequest;
import br.edu.iff.ccc.devteck.dto.ItemCarrinhoView;
import br.edu.iff.ccc.devteck.services.CarrinhoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/carrinho")
@Tag(name = "Carrinho", description = "Carrinho de compras do cliente")
public class CarrinhoRestController {

    private final CarrinhoUseCase carrinhoUseCase;

    public CarrinhoRestController(CarrinhoUseCase carrinhoUseCase) {
        this.carrinhoUseCase = carrinhoUseCase;
    }

    @GetMapping("/{clienteId}")
    @Operation(summary = "Lista os itens do carrinho de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    public ResponseEntity<List<ItemCarrinhoView>> listar(@PathVariable Long clienteId) {
        return ResponseEntity.ok(carrinhoUseCase.listarItens(clienteId));
    }

    @PostMapping("/{clienteId}/itens")
    @Operation(summary = "Adiciona um produto ao carrinho do cliente",
            description = "Se o produto já estiver no carrinho, a quantidade é somada à existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item adicionado, carrinho atualizado retornado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<List<ItemCarrinhoView>> adicionar(@PathVariable Long clienteId,
                                                             @Valid @RequestBody ItemCarrinhoRequest request) {
        carrinhoUseCase.adicionarProduto(clienteId, request.getProdutoId(), request.getQuantidade());
        return ResponseEntity.ok(carrinhoUseCase.listarItens(clienteId));
    }

    @PutMapping("/{clienteId}/itens/{produtoId}")
    @Operation(summary = "Define a quantidade de um item no carrinho",
            description = "Quantidade 0 ou negativa remove o item do carrinho.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Quantidade atualizada, carrinho retornado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<List<ItemCarrinhoView>> atualizarQuantidade(@PathVariable Long clienteId,
                                                                       @PathVariable Long produtoId,
                                                                       @RequestBody ItemCarrinhoRequest request) {
        carrinhoUseCase.atualizarQuantidade(clienteId, produtoId, request.getQuantidade());
        return ResponseEntity.ok(carrinhoUseCase.listarItens(clienteId));
    }

    @DeleteMapping("/{clienteId}/itens/{produtoId}")
    @Operation(summary = "Remove um item específico do carrinho")
    @ApiResponse(responseCode = "204", description = "Item removido")
    public ResponseEntity<Void> remover(@PathVariable Long clienteId, @PathVariable Long produtoId) {
        carrinhoUseCase.removerProduto(clienteId, produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{clienteId}")
    @Operation(summary = "Esvazia todo o carrinho do cliente")
    @ApiResponse(responseCode = "204", description = "Carrinho esvaziado")
    public ResponseEntity<Void> limpar(@PathVariable Long clienteId) {
        carrinhoUseCase.limparCarrinho(clienteId);
        return ResponseEntity.noContent().build();
    }

}
