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

import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.model.Genero;

@RestController
@RequestMapping("/api/generos")
public class GeneroController {

    @Autowired
    private GeneroService generoService;

    @GetMapping
    public List<Genero> buscarTodos() {
        return generoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Genero buscarPorId(@PathVariable Long id) {
        return generoService.buscarPorId(id);
    }

    @PostMapping
    public Genero salvar(@RequestBody Genero genero) {
        return generoService.salvar(genero);
    }

    @PutMapping("/{id}")
    public Genero atualizar(@PathVariable Long id, @RequestBody Genero genero) {
        return generoService.atualizar(id, genero);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        generoService.deletar(id);
    }
}

