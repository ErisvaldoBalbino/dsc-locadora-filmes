package ifrn.edu.br.locadora_filmes.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.FilmeUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Genero;

import java.util.List;

@Service
public class FilmeService {

    @Autowired
    private FilmeRepository filmeRepository;

    @Autowired
    private GeneroRepository generoRepository;

    public List<FilmeResponseDTO> buscarTodos() {
        return filmeRepository.findAll().stream()
            .map(this::converterParaDTO)
            .toList();
    }

    public Filme buscarPorId(Long id) {
        return filmeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado."));
    }

    public FilmeResponseDTO buscarPorIdDTO(Long id) {
        Filme filme = filmeRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Filme não encontrado."));
        return converterParaDTO(filme);
    }

    public List<FilmeResponseDTO> buscarDisponiveis() {
        return filmeRepository.findFilmesDisponiveis().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public FilmeResponseDTO salvar(FilmeCreateDTO filmeDTO) {
        if (filmeRepository.existsByTitulo(filmeDTO.getTitulo())) {
            throw new RuntimeException("Já existe um filme com esse título.");
        }

        Genero genero = generoRepository.findById(filmeDTO.getGeneroId())
            .orElseThrow(() -> new RuntimeException("Genêro não encontrado."));

        Filme filme = new Filme();
        filme.setTitulo(filmeDTO.getTitulo());
        filme.setAno(filmeDTO.getAno());
        filme.setQuantidade_total(filmeDTO.getQuantidade_total());
        filme.setGenero(genero);

        Filme filmeSalvo = filmeRepository.save(filme);

        return converterParaDTO(filmeSalvo);
    }

    @Transactional
    public FilmeResponseDTO atualizar(Long id, FilmeUpdateDTO filmeDTO) {
        Filme filmeExistente = buscarPorId(id);
        if (!filmeExistente.getTitulo().equals(filmeDTO.getTitulo()) && 
            filmeRepository.existsByTitulo(filmeDTO.getTitulo())) {
            throw new RuntimeException("Já existe um filme com esse título.");
        }

        Genero genero = generoRepository.findById(filmeDTO.getGeneroId())
        .orElseThrow(() -> new RuntimeException("Genêro não encontrado."));

        filmeExistente.setTitulo(filmeDTO.getTitulo());
        filmeExistente.setAno(filmeDTO.getAno());
        filmeExistente.setGenero(genero);
        filmeExistente.setQuantidade_total(filmeDTO.getQuantidade_total());
        
        Filme filmeAtualizado = filmeRepository.save(filmeExistente);
    
        return converterParaDTO(filmeAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Filme filme = buscarPorId(id);
        filmeRepository.delete(filme);
    }

    private FilmeResponseDTO converterParaDTO(Filme filme) {
        FilmeResponseDTO filmeDTO = new FilmeResponseDTO();

        filmeDTO.setId(filme.getId());
        filmeDTO.setTitulo(filme.getTitulo());
        filmeDTO.setAno(filme.getAno());
        filmeDTO.setQuantidade_total(filme.getQuantidade_total());
        filmeDTO.setGenero(filme.getGenero().getNome());

        return filmeDTO;
    }
}
