package br.edu.iff.ccc.devteck.controller.view;

import br.edu.iff.ccc.devteck.entities.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainViewController {

    @GetMapping("/")
    public String raiz(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            return "redirect:/login";
        }

        if ("ADMIN".equals(usuarioLogado.getTipo())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/produtos";
    }

}
