package ifrn.edu.br.locadora_filmes.dto.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Dados de uma locação")
public class LocacaoResponseDTO {
    @Schema(description = "ID da locação", example = "1")
    private Long id;

    @Schema(description = "Dados do cliente")
    private ClienteResponseDTO cliente;

    @Schema(description = "Data da locação", example = "2026-02-09")
    private LocalDate dataLocacao;

    @Schema(description = "Data prevista para devolução", example = "2026-02-16")
    private LocalDate dataDevolucaoPrevista;

    @Schema(description = "Data real da devolução (null se ainda não devolvido)", example = "2026-02-15", nullable = true)
    private LocalDate dataDevolucaoReal;

    @Schema(description = "Status da locação", example = "ATIVA")
    private StatusLocacao status;

    @Schema(description = "Lista de filmes locados")
    private List<FilmeResponseDTO> filmes;
}
