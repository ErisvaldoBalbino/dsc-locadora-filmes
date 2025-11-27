package ifrn.edu.br.locadora_filmes.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FilmeUpdateDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    private String titulo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1895, message = "Ano deve ser a partir de 1895")
    private int ano;
    
    @NotNull(message = "Quantidade total é obrigatória")
    @Min(value = 0, message = "Quantidade total não pode ser negativa")
    private int quantidade_total;
    
    @NotNull(message = "Gênero é obrigatório")
    private Long generoId;

}
