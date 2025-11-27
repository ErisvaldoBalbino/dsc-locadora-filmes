package ifrn.edu.br.locadora_filmes.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.model.Filme;

import java.util.List;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    public List<Filme> buscarTodos() {
        return filmeRepository.findAll();
    }

    public Filme buscarPorId(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado."));
    }

    @Transactional
    public Filme salvar(Filme filme) {
        if (filmeRepository.existsByTitulo(filme.getTitulo())) {
            throw new RuntimeException("Já existe um filme com esse título.");
        }

        return filmeRepository.save(filme);
    }

    @Transactional
    public Filme atualizar(Long id, Filme filmeAtualizado) {
        Filme filmeExistente = buscarPorId(id);
        if (!filmeExistente.getTitulo().equals(filmeAtualizado.getTitulo()) && 
            filmeRepository.existsByTitulo(filmeAtualizado.getTitulo())) {
            throw new RuntimeException("Já existe um filme com esse título.");
        }
        filmeExistente.setTitulo(filmeAtualizado.getTitulo());
        filmeExistente.setAno(filmeAtualizado.getAno());
        filmeExistente.setGenero(filmeAtualizado.getGenero());
        filmeExistente.setQuantidade_total(filmeAtualizado.getQuantidade_total());
        return filmeRepository.save(filmeExistente);
    }

    @Transactional
    public void deletar(Long id) {
        Filme filme = buscarPorId(id);
        filmeRepository.delete(filme);
    }
}
