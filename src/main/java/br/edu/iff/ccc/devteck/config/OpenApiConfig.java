package br.edu.iff.ccc.devteck.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI devteckOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevTeck")
                        .description("API REST do sistema de catálogo de vendas (Lojinha de Bairro), "
                                + "desenvolvida para a disciplina de Programação Web - BSI/IFF. "
                                + "Expõe os recursos de Produtos, Clientes, Pedidos e Carrinho. "
                                + "As respostas de erro seguem o padrão RFC 9457 (Problem Details for HTTP APIs).")
                        .version("v1")
                        .contact(new Contact().name("DevTeck").email("contato@devteck.com")));
    }

}
