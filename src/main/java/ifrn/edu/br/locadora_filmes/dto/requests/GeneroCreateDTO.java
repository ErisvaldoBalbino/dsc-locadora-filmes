package ifrn.edu.br.locadora_filmes.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GeneroCreateDTO {
    @NotBlank(message = "Nome do gênero é obrigatório")
    @Size(min = 1, max = 50, message = "Nome do gênero deve ter entre 1 e 50 caracteres")
    private String nome;
}
