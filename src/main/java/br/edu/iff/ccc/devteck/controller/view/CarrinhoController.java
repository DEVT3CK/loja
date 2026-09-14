package br.edu.iff.ccc.devteck.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.services.CarrinhoUseCase;
import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    private final CarrinhoUseCase carrinhoUseCase;

    public CarrinhoController(CarrinhoUseCase carrinhoUseCase) {
        this.carrinhoUseCase = carrinhoUseCase;
    }

    @GetMapping
    public String ver(HttpSession session, Model model) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("itens", carrinhoUseCase.listarItens(usuario.getId()));
        model.addAttribute("carrinho", carrinhoUseCase.buscarCarrinho(usuario.getId()));
        return "carrinho";
    }

    @PostMapping("/adicionar")
    public String adicionar(@RequestParam Long produtoId,
                             @RequestParam(defaultValue = "1") int quantidade,
                             HttpSession session) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        carrinhoUseCase.adicionarProduto(usuario.getId(), produtoId, quantidade);
        return "redirect:/carrinho";
    }

    @PostMapping("/remover")
    public String remover(@RequestParam Long produtoId, HttpSession session) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        carrinhoUseCase.removerProduto(usuario.getId(), produtoId);
        return "redirect:/carrinho";
    }

    @PostMapping("/atualizar")
    public String atualizar(@RequestParam Long produtoId, @RequestParam int quantidade, HttpSession session) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        carrinhoUseCase.atualizarQuantidade(usuario.getId(), produtoId, quantidade);
        return "redirect:/carrinho";
    }

    private Usuario usuarioLogado(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogado");
    }

}
