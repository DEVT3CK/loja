// NOVO ARQUIVO: services/DadosIniciais.java
package br.edu.iff.ccc.devteck.services;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.edu.iff.ccc.devteck.entities.Admin;
import br.edu.iff.ccc.devteck.entities.Cliente;
import br.edu.iff.ccc.devteck.repository.UsuarioRepositorio;

@Component
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;

    public DadosIniciais(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public void run(String... args) {
        usuarioRepositorio.save(new Admin(null, "Admin DevTeck", "admin@devteck.com", "admin123",
                LocalDate.now(), "TOTAL"));

        usuarioRepositorio.save(new Cliente(null, "Joao Silva", "joao@email.com", "123456", LocalDate.now(),
                "22999999999", "Rua das Flores, 123", "28000-000", "Campos dos Goytacazes", "RJ"));
    }
}
