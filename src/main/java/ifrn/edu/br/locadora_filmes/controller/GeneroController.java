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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.GeneroResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Genero;

@RestController
@RequestMapping("/api/generos")
@Tag(name = "Gêneros", description = "API para gerenciamento de gêneros de filmes")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    @Operation(
        summary = "Listar todos os gêneros",
        description = "Retorna uma lista com todos os gêneros"
    )
    public List<Genero> buscarTodos() {
        return generoService.buscarTodos();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar gênero por ID",
        description = "Retorna os detalhes de um gênero"
    )
    public Genero buscarPorId(@PathVariable Long id) {
        return generoService.buscarPorId(id);
    }

    @PostMapping
    @Operation(
        summary = "Criar um novo gênero",
        description = "Cria um novo gênero no sistema"
    )
    public GeneroResponseDTO salvar(@Valid @RequestBody GeneroCreateDTO generoDTO) {
        return generoService.salvar(generoDTO);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar um gênero",
        description = "Atualiza um gênero existente"
    )
    public GeneroResponseDTO atualizar(@PathVariable Long id, @RequestBody GeneroUpdateDTO generoDTO) {
        return generoService.atualizar(id, generoDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar um gênero",
        description = "Deleta um gênero existente, o gênero não poderá ser deletado se possuir filmes associados"
    )
    public void deletar(@PathVariable Long id) {
        generoService.deletar(id);
    }
}

