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

import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.GeneroResponseDTO;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gêneros", description = "API para gerenciamento de gêneros de filmes")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    @Operation(summary = "Listar todos os gêneros", description = "Retorna uma lista com todos os gêneros cadastrados", responses = @ApiResponse(responseCode = "200", description = "Lista de gêneros retornada com sucesso"))
    public ResponseEntity<List<GeneroResponseDTO>> buscarTodos() {
        List<GeneroResponseDTO> generos = generoService.buscarTodos();
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar gênero por ID", description = "Retorna os detalhes de um gênero específico", responses = {
            @ApiResponse(responseCode = "200", description = "Gênero encontrado"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Gênero não encontrado.\"}")))
    })
    public ResponseEntity<GeneroResponseDTO> buscarPorId(
            @Parameter(description = "ID do gênero", example = "1") @PathVariable Long id) {
        GeneroResponseDTO genero = generoService.buscarPorIdDTO(id);
        return ResponseEntity.ok(genero);
    }

    @PostMapping
    @Operation(summary = "Criar um novo gênero", description = "Cria um novo gênero no sistema. O nome deve ser único.", responses = {
            @ApiResponse(responseCode = "200", description = "Gênero criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nome já existente ou dados inválidos", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Já existe um gênero com esse nome.\"}")))
    })
    public ResponseEntity<GeneroResponseDTO> salvar(@Valid @RequestBody GeneroCreateDTO generoDTO) {
        GeneroResponseDTO genero = generoService.salvar(generoDTO);
        return ResponseEntity.ok(genero);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um gênero", description = "Atualiza o nome de um gênero existente", responses = {
            @ApiResponse(responseCode = "200", description = "Gênero atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado"),
            @ApiResponse(responseCode = "400", description = "Nome já existente")
    })
    public ResponseEntity<GeneroResponseDTO> atualizar(
            @Parameter(description = "ID do gênero", example = "1") @PathVariable Long id,
            @RequestBody GeneroUpdateDTO generoDTO) {
        GeneroResponseDTO genero = generoService.atualizar(id, generoDTO);
        return ResponseEntity.ok(genero);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um gênero", description = "Deleta um gênero. Não é possível deletar gêneros com filmes associados.", responses = {
            @ApiResponse(responseCode = "204", description = "Gênero deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Gênero não encontrado"),
            @ApiResponse(responseCode = "400", description = "Gênero possui filmes associados", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Não é possível deletar um gênero com filmes associados.\"}")))
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do gênero", example = "1") @PathVariable Long id) {
        generoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
