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

import br.edu.iff.ccc.devteck.dto.ProdutoRequest;
import br.edu.iff.ccc.devteck.entities.Produto;
import br.edu.iff.ccc.devteck.services.ProdutoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/produtos")
@Tag(name = "Produtos", description = "Cadastro, consulta e controle de estoque dos produtos")
public class ProdutoRestController {

    private final ProdutoUseCase produtoUseCase;

    public ProdutoRestController(ProdutoUseCase produtoUseCase) {
        this.produtoUseCase = produtoUseCase;
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<Produto>> listar() {
        return ResponseEntity.ok(produtoUseCase.listarProdutos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(produtoUseCase.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo produto",
            description = "Se já existir um produto DESATIVADO com o mesmo nome, ele é reativado e "
                    + "atualizado com os dados enviados, em vez de criar um novo registro. "
                    + "Se já existir um produto ATIVO com o mesmo nome, retorna 409 Conflict.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Produto criado ou reativado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Já existe um produto ATIVO com este nome")
    })
    public ResponseEntity<Produto> cadastrar(@Valid @RequestBody ProdutoRequest request) {
        Produto criado = produtoUseCase.cadastrarProduto(request);
        URI location = URI.create("/api/v1/produtos/" + criado.getId());
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Produto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        Produto atualizado = produtoUseCase.atualizarProduto(id, request);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove (desativa) um produto")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Produto removido"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoUseCase.removerProduto(id);
        return ResponseEntity.noContent().build();
    }

}
