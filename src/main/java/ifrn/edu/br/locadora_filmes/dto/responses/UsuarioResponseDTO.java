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
@Schema(description = "Dados do usuário autenticado")
public class UsuarioResponseDTO {
    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @Schema(description = "Nome do usuário", example = "Admin Locadora")
    private String nome;

    @Schema(description = "Email do usuário", example = "admin@locadora.com")
    private String email;
}
