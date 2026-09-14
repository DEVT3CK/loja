package br.edu.iff.ccc.devteck.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.ccc.devteck.services.ProdutoUseCase;


@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoUseCase produtoUseCase;

    public ProdutoController(ProdutoUseCase produtoUseCase) {
        this.produtoUseCase = produtoUseCase;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String categoria, Model model) {
        var todosProdutos = produtoUseCase.listarProdutos();

        var categorias = todosProdutos.stream()
                .map(p -> p.getCategoria())
                .distinct()
                .sorted()
                .toList();

        var produtos = todosProdutos;
        if (categoria != null && !categoria.isBlank()) {
            produtos = produtos.stream()
                    .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                    .toList();
        }

        model.addAttribute("produtos", produtos);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaSelecionada", categoria);
        return "produtos";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoUseCase.buscarPorId(id));
        return "produtoDetalhe";
    }

}
