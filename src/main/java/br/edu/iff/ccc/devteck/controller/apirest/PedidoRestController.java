package br.edu.iff.ccc.devteck.controller.apirest;

import java.net.URI;
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

import br.edu.iff.ccc.devteck.dto.PedidoRequest;
import br.edu.iff.ccc.devteck.dto.StatusRequest;
import br.edu.iff.ccc.devteck.entities.Pedido;
import br.edu.iff.ccc.devteck.services.PedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Finalização, acompanhamento e cancelamento de pedidos")
public class PedidoRestController {

    private final PedidoUseCase pedidoUseCase;

    public PedidoRestController(PedidoUseCase pedidoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todos os pedidos (visão administrativa)")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoUseCase.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um pedido pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoUseCase.buscarPorId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Lista o histórico de pedidos de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso (pode ser vazia)")
    public ResponseEntity<List<Pedido>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoUseCase.listarPorCliente(clienteId));
    }

    @PostMapping
    @Operation(summary = "Finaliza o pedido a partir do carrinho atual do cliente",
            description = "Cria o pedido com os itens que estão no carrinho do cliente informado, "
                    + "valida e dá baixa no estoque de cada produto, e esvazia o carrinho ao final.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: endereço em branco)"),
            @ApiResponse(responseCode = "422", description = "Carrinho vazio ou estoque insuficiente para algum item")
    })
    public ResponseEntity<Pedido> finalizar(@Valid @RequestBody PedidoRequest request) {
        Pedido pedido = pedidoUseCase.finalizarPedido(request.getClienteId(), request.getEnderecoEntrega());
        URI location = URI.create("/api/v1/pedidos/" + pedido.getId());
        return ResponseEntity.created(location).body(pedido);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Atualiza o status de um pedido (uso administrativo)",
            description = "Ex: PENDENTE -> PAGO -> ENVIADO -> ENTREGUE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "400", description = "Status inválido"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Pedido> atualizarStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        pedidoUseCase.atualizarStatus(id, request.getStatus());
        return ResponseEntity.ok(pedidoUseCase.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela um pedido",
            description = "Cancela o pedido e devolve as quantidades dos itens ao estoque dos produtos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoUseCase.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

}
