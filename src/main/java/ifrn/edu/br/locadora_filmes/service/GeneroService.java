package ifrn.edu.br.locadora_filmes.service;

import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.GeneroResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.ResourceNotFoundException;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Genero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;

@Service
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<GeneroResponseDTO> buscarTodos() {
        return generoRepository.findAll().stream()
            .map(this::converterParaDTO)
            .toList();
    }


    public Genero buscarPorId(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado."));
    }

    public GeneroResponseDTO buscarPorIdDTO(Long id) {
        Genero genero = generoRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));
        return converterParaDTO(genero);
    }

    @Transactional
    public GeneroResponseDTO salvar(GeneroCreateDTO generoDTO) {
        if (generoRepository.existsByNome(generoDTO.getNome())) {
            throw new BusinessException("Já existe um gênero com esse nome.");
        }

        Genero genero = new Genero();

        genero.setNome(generoDTO.getNome());

        Genero generoSalvo = generoRepository.save(genero);

        return converterParaDTO(generoSalvo);
    }

    @Transactional
    public GeneroResponseDTO atualizar(Long id, GeneroUpdateDTO generoDTO) {
        Genero generoExistente = buscarPorId(id);

        if (!generoExistente.getNome().equals(generoDTO.getNome()) && 
             generoRepository.existsByNome(generoDTO.getNome())) {
            throw new BusinessException("Já existe um gênero com esse nome.");
        }

        generoExistente.setNome(generoDTO.getNome());

        Genero generoAtualizado = generoRepository.save(generoExistente);
        
        return converterParaDTO(generoAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Genero genero = buscarPorId(id);
        generoRepository.delete(genero);
    }

    private GeneroResponseDTO converterParaDTO(Genero genero) {
        GeneroResponseDTO generoDTO = new GeneroResponseDTO();

        generoDTO.setId(genero.getId());
        generoDTO.setNome(genero.getNome());

        return generoDTO;
    }
}