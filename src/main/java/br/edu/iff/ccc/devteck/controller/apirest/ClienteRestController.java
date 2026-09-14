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

import br.edu.iff.ccc.devteck.dto.ClienteRequest;
import br.edu.iff.ccc.devteck.entities.Cliente;
import br.edu.iff.ccc.devteck.services.ClienteUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Cadastro e gerenciamento dos clientes da loja")
public class ClienteRestController {

    private final ClienteUseCase clienteUseCase;

    public ClienteRestController(ClienteUseCase clienteUseCase) {
        this.clienteUseCase = clienteUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todos os clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteUseCase.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteUseCase.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Já existe um cliente cadastrado com este email")
    })
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody ClienteRequest request) {
        Cliente criado = clienteUseCase.cadastrar(request);
        URI location = URI.create("/api/v1/clientes/" + criado.getId());
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um cliente existente",
            description = "O campo 'senha' é opcional na atualização: se vier em branco, a senha atual é mantida "
                    + "(por isso este endpoint não usa @Valid — a validação completa é feita só na criação).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteUseCase.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        clienteUseCase.buscarPorId(id); // garante 404 padronizado (RFC 9457) se o id nao existir
        clienteUseCase.remover(id);
        return ResponseEntity.noContent().build();
    }

}
