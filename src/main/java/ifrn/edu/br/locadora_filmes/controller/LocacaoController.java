package ifrn.edu.br.locadora_filmes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import ifrn.edu.br.locadora_filmes.dto.requests.LocacaoCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.LocacaoResponseDTO;
import ifrn.edu.br.locadora_filmes.service.LocacaoService;

@RestController
@RequestMapping("/api/locacoes")
@Tag(name = "Locações", description = "API para gerenciamento de locações de filmes")
public class LocacaoController {

    @Autowired
    private LocacaoService locacaoService;

    @GetMapping
    @Operation(summary = "Listar todas as locações", description = "Retorna uma lista com todas as locações do sistema", responses = @ApiResponse(responseCode = "200", description = "Lista de locações retornada com sucesso"))
    public ResponseEntity<List<LocacaoResponseDTO>> buscarTodos() {
        List<LocacaoResponseDTO> locacoes = locacaoService.buscarTodos();
        return ResponseEntity.ok(locacoes);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar locação por ID", description = "Retorna os detalhes de uma locação específica", responses = {
            @ApiResponse(responseCode = "200", description = "Locação encontrada"),
            @ApiResponse(responseCode = "404", description = "Locação não encontrada", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Locação não encontrada.\"}")))
    })
    public ResponseEntity<LocacaoResponseDTO> buscarPorId(
            @Parameter(description = "ID da locação", example = "1") @PathVariable Long id) {
        LocacaoResponseDTO locacao = locacaoService.buscarPorIdDTO(id);
        return ResponseEntity.ok(locacao);
    }

    @GetMapping("/atrasadas")
    @Operation(summary = "Listar locações atrasadas", description = "Retorna todas as locações com data de devolução prevista ultrapassada e ainda não devolvidas", responses = @ApiResponse(responseCode = "200", description = "Lista de locações atrasadas"))
    public ResponseEntity<List<LocacaoResponseDTO>> buscarAtrasadas() {
        List<LocacaoResponseDTO> locacoes = locacaoService.buscarAtrasadas();
        return ResponseEntity.ok(locacoes);
    }

    @PostMapping()
    @Operation(summary = "Criar uma nova locação", description = "Registra uma nova locação. Informe o cliente, dias de locação e os filmes desejados. O estoque dos filmes é decrementado automaticamente.", responses = {
            @ApiResponse(responseCode = "200", description = "Locação criada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente ou filme não encontrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado.\"}"))),
            @ApiResponse(responseCode = "400", description = "Filme sem cópias disponíveis", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Filme 'Matrix' não possui cópias disponíveis.\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido")
    })
    public ResponseEntity<LocacaoResponseDTO> salvar(@Valid @RequestBody LocacaoCreateDTO locacaoDTO) {
        LocacaoResponseDTO locacao = locacaoService.salvar(locacaoDTO);
        return ResponseEntity.ok(locacao);
    }

    @PatchMapping("{id}/devolver")
    @Operation(summary = "Registrar devolução", description = "Registra a devolução dos filmes. O status muda para FINALIZADA e o estoque dos filmes é reposto.", responses = {
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Locação não encontrada"),
            @ApiResponse(responseCode = "400", description = "Locação já foi finalizada", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Esta locação já foi finalizada.\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<LocacaoResponseDTO> devolver(
            @Parameter(description = "ID da locação", example = "1") @PathVariable Long id) {
        LocacaoResponseDTO locacao = locacaoService.devolver(id);
        return ResponseEntity.ok(locacao);
    }
}
