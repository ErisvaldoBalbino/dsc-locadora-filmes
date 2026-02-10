package ifrn.edu.br.locadora_filmes.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para criação de um cliente")
public class ClienteCreateDTO {
    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 1, max = 50, message = "Nome deve ter entre 1 e 50 caracteres")
    @Schema(description = "Nome completo do cliente", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter um formato válido")
    @Schema(description = "Email do cliente", example = "joao.silva@email.com")
    private String email;
}
