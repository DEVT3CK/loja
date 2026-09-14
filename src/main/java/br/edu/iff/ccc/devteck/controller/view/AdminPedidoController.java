package br.edu.iff.ccc.devteck.controller.view;

import br.edu.iff.ccc.devteck.services.PedidoUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {

    private final PedidoUseCase pedidoUseCase;

    public AdminPedidoController(PedidoUseCase pedidoUseCase) {
        this.pedidoUseCase = pedidoUseCase;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoUseCase.listarTodos());
        return "adminPedidos";
    }

    @PostMapping("/{id}/status")
    public String atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        pedidoUseCase.atualizarStatus(id, status);
        return "redirect:/admin/pedidos";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id) {
        // Delete de Pedido: cancela e devolve os itens ao estoque
        pedidoUseCase.cancelarPedido(id);
        return "redirect:/admin/pedidos";
    }

}
