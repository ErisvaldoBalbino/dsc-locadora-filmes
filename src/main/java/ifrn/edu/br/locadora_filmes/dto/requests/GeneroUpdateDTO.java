package ifrn.edu.br.locadora_filmes.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para atualização de um gênero")
public class GeneroUpdateDTO {
    @NotBlank(message = "Nome do gênero é obrigatório")
    @Size(min = 1, max = 50, message = "Nome do gênero deve ter entre 1 e 50 caracteres")
    @Schema(description = "Novo nome do gênero", example = "Ficção Científica")
    private String nome;
}
