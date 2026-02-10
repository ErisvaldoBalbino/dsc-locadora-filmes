package ifrn.edu.br.locadora_filmes.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de um gênero")
public class GeneroResponseDTO {
    @Schema(description = "ID do gênero", example = "1")
    private Long id;

    @Schema(description = "Nome do gênero", example = "Ação")
    private String nome;
}
