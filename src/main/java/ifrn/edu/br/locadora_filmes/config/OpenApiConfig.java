package ifrn.edu.br.locadora_filmes.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Locadora de Filmes API")
                        .version("1.0.0")
                        .description(
                                """
                                        API REST para gerenciamento de uma locadora de filmes.

                                        Para acessar endpoints protegidos:
                                        1. Registre-se ou faça login em `/api/auth/register` ou `/api/auth/login`
                                        2. Copie o token retornado
                                        3. Clique no botão **Authorize** 🔓 acima
                                        4. Cole o token no campo
                                        5. Clique em **Authorize**

                                        """))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description(
                                                "Insira o token JWT obtido no login. Exemplo: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")));
    }
}
