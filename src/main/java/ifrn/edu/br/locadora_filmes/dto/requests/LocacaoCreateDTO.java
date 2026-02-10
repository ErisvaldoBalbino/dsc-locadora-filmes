package ifrn.edu.br.locadora_filmes.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Dados para criação de uma locação")
public class LocacaoCreateDTO {
    @NotNull(message = "Cliente é obrigatório")
    @Schema(description = "ID do cliente", example = "1")
    private Long clienteId;

    @NotNull(message = "Data de devolução prevista é obrigatória")
    @Schema(description = "Quantidade de dias para devolução", example = "7")
    private Integer diasLocacao;

    @NotEmpty(message = "É necessário pelo menos um filme na locação")
    @Schema(description = "Lista de IDs dos filmes", example = "[1, 2, 3]")
    private List<Long> filmesIds;
}
