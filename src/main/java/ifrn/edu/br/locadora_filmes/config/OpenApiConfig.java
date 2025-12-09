package ifrn.edu.br.locadora_filmes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Locadora de Filmes API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de uma locadora de filmes. " +
                                "Permite gerenciar filmes, gêneros, clientes e locações."));
    }
}

