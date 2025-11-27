package ifrn.edu.br.locadora_filmes.service;

import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.model.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<Genero> buscarTodos() {
        return generoRepository.findAll();
    }

    public Genero buscarPorId(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero não encontrado."));
    }

    @Transactional
    public Genero salvar(Genero genero) {
        if (generoRepository.existsByNome(genero.getNome())) {
            throw new RuntimeException("Já existe um gênero com esse nome.");
        }
        return generoRepository.save(genero);
    }

    @Transactional
    public Genero atualizar(Long id, Genero generoAtualizado) {
        Genero generoExistente = buscarPorId(id);

        if (!generoExistente.getNome().equals(generoAtualizado.getNome()) && 
             generoRepository.existsByNome(generoAtualizado.getNome())) {
            throw new RuntimeException("Já existe um gênero com esse nome.");
        }

        generoExistente.setNome(generoAtualizado.getNome());
        
        return generoRepository.save(generoExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Genero genero = buscarPorId(id);
        generoRepository.delete(genero);
    }
}