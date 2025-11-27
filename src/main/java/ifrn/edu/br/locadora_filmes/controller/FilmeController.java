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
    public List<Filme> buscarTodos() {
        return filmeService.buscarTodos();
    }
    
    @GetMapping("{id}")
    @Operation(
        summary = "Buscar filme por ID",
        description = "Retorna os detalhes de um filme"
    )
    public Filme buscarPorId(@PathVariable Long id) {
        return filmeService.buscarPorId(id);
    }

    @PostMapping()
    @Operation(
        summary = "Criar um novo filme",
        description = "Cria um novo filme no sistema"
    )
    public Filme salvar(@RequestBody Filme filme) {
        return filmeService.salvar(filme);
    }
    
    @PutMapping("{id}")
    @Operation(
        summary = "Atualizar um filme",
        description = "Atualiza um filme existente"
    )
    public Filme atualizar(@PathVariable Long id, @RequestBody Filme filme) {
        return filmeService.atualizar(id, filme);
    }
    
    @DeleteMapping("{id}")
    @Operation(
        summary = "Deletar um filme",
        description = "Deleta um filme, caso possua locações associadas o filme não pode ser deletado"
    )
    public void deletar(@PathVariable Long id) {
        filmeService.deletar(id);
    }
}
