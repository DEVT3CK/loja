package br.edu.iff.ccc.devteck.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.edu.iff.ccc.devteck.dto.ProdutoRequest;
import br.edu.iff.ccc.devteck.services.ProdutoUseCase;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/produtos")
public class AdminProdutoController {

    private final ProdutoUseCase produtoUseCase;

    public AdminProdutoController(ProdutoUseCase produtoUseCase) {
        this.produtoUseCase = produtoUseCase;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoUseCase.listarProdutos());
        return "adminProdutos";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("produtoRequest", new ProdutoRequest());
        return "adminProdutoForm";
    }

    @PostMapping
    public String salvar(@Valid ProdutoRequest produtoRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "adminProdutoForm";
        }
        produtoUseCase.cadastrarProduto(produtoRequest);
        return "redirect:/admin/produtos";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        var produto = produtoUseCase.buscarPorId(id);
        model.addAttribute("produtoRequest", produtoUseCase.paraRequest(produto));
        model.addAttribute("produtoId", id);
        return "adminProdutoForm";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id, @Valid ProdutoRequest produtoRequest,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("produtoId", id);
            return "adminProdutoForm";
        }
        produtoUseCase.atualizarProduto(id, produtoRequest);
        return "redirect:/admin/produtos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        produtoUseCase.removerProduto(id);
        return "redirect:/admin/produtos";
    }

}
