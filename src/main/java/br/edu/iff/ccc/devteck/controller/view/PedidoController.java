package br.edu.iff.ccc.devteck.controller.view;

import br.edu.iff.ccc.devteck.entities.Usuario;
import br.edu.iff.ccc.devteck.exceptions.RegraDeNegocioException;
import br.edu.iff.ccc.devteck.services.CarrinhoUseCase;
import br.edu.iff.ccc.devteck.services.PedidoUseCase;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PedidoController {

    private final PedidoUseCase pedidoUseCase;
    private final CarrinhoUseCase carrinhoUseCase;

    public PedidoController(PedidoUseCase pedidoUseCase, CarrinhoUseCase carrinhoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
        this.carrinhoUseCase = carrinhoUseCase;
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("itens", carrinhoUseCase.listarItens(usuario.getId()));
        model.addAttribute("carrinho", carrinhoUseCase.buscarCarrinho(usuario.getId()));
        return "checkout";
    }

    @PostMapping("/pedidos")
    public String finalizar(@RequestParam String enderecoEntrega, HttpSession session, Model model) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        try {
            pedidoUseCase.finalizarPedido(usuario.getId(), enderecoEntrega);
            return "redirect:/pedidos";
        } catch (RegraDeNegocioException e) {
            // Ex: carrinho vazio ou estoque insuficiente - volta pro
            // checkout mostrando o erro, sem perder os dados do carrinho
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("itens", carrinhoUseCase.listarItens(usuario.getId()));
            model.addAttribute("carrinho", carrinhoUseCase.buscarCarrinho(usuario.getId()));
            return "checkout";
        }
    }

    @GetMapping("/pedidos")
    public String historico(HttpSession session, Model model) {
        Usuario usuario = usuarioLogado(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        model.addAttribute("pedidos", pedidoUseCase.listarPorCliente(usuario.getId()));
        return "pedidos";
    }

    private Usuario usuarioLogado(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioLogado");
    }

}
