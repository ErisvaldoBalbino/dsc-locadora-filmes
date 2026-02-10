package ifrn.edu.br.locadora_filmes.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta de autenticação com token JWT")
public class AuthResponseDTO {
    @Schema(description = "Token JWT para autenticação", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBsb2NhZG9yYS5jb20iLCJpYXQiOjE3MDc1...")
    private String token;

    @Schema(description = "Tipo do token", example = "Bearer")
    private String tipo;

    @Schema(description = "Tempo de expiração em milissegundos", example = "86400000")
    private Long expiresIn;

    @Schema(description = "Dados do usuário")
    private UsuarioResponseDTO usuario;
}
