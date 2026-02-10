package ifrn.edu.br.locadora_filmes.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de um cliente")
public class ClienteResponseDTO {
    @Schema(description = "ID do cliente", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "João Silva")
    private String nome;

    @Schema(description = "Email do cliente", example = "joao.silva@email.com")
    private String email;
}
