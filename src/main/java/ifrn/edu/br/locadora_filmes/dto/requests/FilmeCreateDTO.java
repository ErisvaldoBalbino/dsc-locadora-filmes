package ifrn.edu.br.locadora_filmes.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para criação de um filme")
public class FilmeCreateDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    @Schema(description = "Título do filme", example = "Matrix")
    private String titulo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1895, message = "Ano deve ser a partir de 1895")
    @Schema(description = "Ano de lançamento", example = "1999")
    private int ano;

    @NotNull(message = "Quantidade total é obrigatória")
    @Min(value = 0, message = "Quantidade total não pode ser negativa")
    @Schema(description = "Quantidade de cópias disponíveis", example = "5")
    private int quantidade_total;

    @NotNull(message = "Gênero é obrigatório")
    @Schema(description = "ID do gênero do filme", example = "1")
    private Long generoId;
}
