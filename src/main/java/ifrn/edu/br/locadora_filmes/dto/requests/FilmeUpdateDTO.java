package ifrn.edu.br.locadora_filmes.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para atualização de um filme")
public class FilmeUpdateDTO {
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    @Schema(description = "Novo título do filme", example = "Matrix Reloaded")
    private String titulo;

    @Min(value = 1895, message = "Ano deve ser a partir de 1895")
    @Schema(description = "Ano de lançamento", example = "2003")
    private int ano;

    @Min(value = 0, message = "Quantidade total não pode ser negativa")
    @Schema(description = "Quantidade de cópias disponíveis", example = "3")
    private int quantidade_total;

    @Schema(description = "ID do gênero do filme", example = "1")
    private Long generoId;
}
