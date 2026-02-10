package ifrn.edu.br.locadora_filmes.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de um filme")
public class FilmeResponseDTO {
    @Schema(description = "ID do filme", example = "1")
    private Long id;

    @Schema(description = "Título do filme", example = "Matrix")
    private String titulo;

    @Schema(description = "Ano de lançamento", example = "1999")
    private int ano;

    @Schema(description = "Quantidade de cópias disponíveis", example = "5")
    private int quantidade_total;

    @Schema(description = "Nome do gênero", example = "Ação")
    private String genero;
}
