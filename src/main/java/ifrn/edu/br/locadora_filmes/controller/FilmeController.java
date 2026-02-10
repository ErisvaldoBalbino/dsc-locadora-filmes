package ifrn.edu.br.locadora_filmes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import ifrn.edu.br.locadora_filmes.dto.requests.FilmeCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.service.FilmeService;

@RestController
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
public class FilmeController {

    @Autowired
    private FilmeService filmeService;

    @GetMapping
    @Operation(summary = "Listar todos os filmes", description = "Retorna uma lista com todos os filmes cadastrados", responses = @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso"))
    public ResponseEntity<List<FilmeResponseDTO>> buscarTodos() {
        List<FilmeResponseDTO> filmes = filmeService.buscarTodos();
        return ResponseEntity.ok(filmes);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar filme por ID", description = "Retorna os detalhes de um filme específico", responses = {
            @ApiResponse(responseCode = "200", description = "Filme encontrado"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Filme não encontrado.\"}")))
    })
    public ResponseEntity<FilmeResponseDTO> buscarPorId(
            @Parameter(description = "ID do filme", example = "1") @PathVariable Long id) {
        FilmeResponseDTO filme = filmeService.buscarPorIdDTO(id);
        return ResponseEntity.ok(filme);
    }

    @GetMapping("/disponiveis")
    @Operation(summary = "Buscar filmes disponíveis", description = "Retorna filmes com cópias em estoque (quantidade_total > 0)", responses = @ApiResponse(responseCode = "200", description = "Lista de filmes disponíveis"))
    public ResponseEntity<List<FilmeResponseDTO>> buscarDisponiveis() {
        List<FilmeResponseDTO> filmes = filmeService.buscarDisponiveis();
        return ResponseEntity.ok(filmes);
    }

    @PostMapping()
    @Operation(summary = "Criar um novo filme", description = "Cria um novo filme. O título deve ser único e o gênero deve existir.", responses = {
            @ApiResponse(responseCode = "200", description = "Filme criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Título já existente ou dados inválidos", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Já existe um filme com esse título.\"}"))),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Gênero não encontrado.\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido")
    })
    public ResponseEntity<FilmeResponseDTO> salvar(@Valid @RequestBody FilmeCreateDTO filmeDTO) {
        FilmeResponseDTO filme = filmeService.salvar(filmeDTO);
        return ResponseEntity.ok(filme);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um filme", description = "Atualiza os dados de um filme existente", responses = {
            @ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme ou gênero não encontrado"),
            @ApiResponse(responseCode = "400", description = "Título já existente"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<FilmeResponseDTO> atualizar(
            @Parameter(description = "ID do filme", example = "1") @PathVariable Long id,
            @RequestBody FilmeUpdateDTO filmeDTO) {
        FilmeResponseDTO filme = filmeService.atualizar(id, filmeDTO);
        return ResponseEntity.ok(filme);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar um filme", description = "Deleta um filme. Não é possível deletar filmes com locações associadas.", responses = {
            @ApiResponse(responseCode = "204", description = "Filme deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do filme", example = "1") @PathVariable Long id) {
        filmeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
