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
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import ifrn.edu.br.locadora_filmes.dto.requests.FilmeCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.service.FilmeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "API para gerenciamento de filmes")
public class FilmeController {
    
    @Autowired
    private FilmeService filmeService;

    @GetMapping
    @Operation(
        summary = "Listar todos os filmes",
        description = "Retorna uma lista com todos os filmes do sistema"
    )
    public ResponseEntity<List<FilmeResponseDTO>> buscarTodos() {
        List<FilmeResponseDTO> filmes = filmeService.buscarTodos();
        return ResponseEntity.ok(filmes);
    }
    
    @GetMapping("{id}")
    @Operation(
        summary = "Buscar filme por ID",
        description = "Retorna os detalhes de um filme"
    )
    public ResponseEntity<FilmeResponseDTO> buscarPorId(@PathVariable Long id) {
        FilmeResponseDTO filme = filmeService.buscarPorIdDTO(id);
        return ResponseEntity.ok(filme);
    }

    @GetMapping("/disponiveis")
    @Operation(
        summary = "Buscar filmes com cópias em estoque",
        description = "Retorna filmes que possuem quantidade_total > 0"
    )
    public ResponseEntity<List<FilmeResponseDTO>> buscarDisponiveis() {
        List<FilmeResponseDTO> filmes = filmeService.buscarDisponiveis();
        return ResponseEntity.ok(filmes);
    }

    @PostMapping()
    @Operation(
        summary = "Criar um novo filme",
        description = "Cria um novo filme no sistema"
    )
    public ResponseEntity<FilmeResponseDTO> salvar(@Valid @RequestBody FilmeCreateDTO filmeDTO) {
        FilmeResponseDTO filme = filmeService.salvar(filmeDTO);
        return ResponseEntity.ok(filme);
    }
    
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar um filme",
        description = "Atualiza um filme existente"
    )
    public ResponseEntity<FilmeResponseDTO> atualizar(@PathVariable Long id, @RequestBody FilmeUpdateDTO filmeDTO) {
        FilmeResponseDTO filme = filmeService.atualizar(id, filmeDTO);
        return ResponseEntity.ok(filme);
    }
    
    @DeleteMapping("{id}")
    @Operation(
        summary = "Deletar um filme",
        description = "Deleta um filme, caso possua locações associadas o filme não pode ser deletado"
    )
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        filmeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
