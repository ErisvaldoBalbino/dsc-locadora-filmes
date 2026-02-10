package ifrn.edu.br.locadora_filmes.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ifrn.edu.br.locadora_filmes.repository.ClienteRepository;
import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.repository.LocacaoRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.LocacaoCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.ClienteResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.LocacaoResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.ResourceNotFoundException;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Cliente;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Locacao;
import ifrn.edu.br.locadora_filmes.model.LocacaoItem;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FilmeRepository filmeRepository;

    public List<LocacaoResponseDTO> buscarTodos() {
        return locacaoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Locacao buscarPorId(Long id) {
        return locacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locação não encontrada."));
    }

    public LocacaoResponseDTO buscarPorIdDTO(Long id) {
        Locacao locacao = locacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Locação não encontrada."));
        return converterParaDTO(locacao);
    }

    public List<LocacaoResponseDTO> buscarAtrasadas() {
        LocalDate hoje = LocalDate.now();

        atualizarStatusAtrasadas(hoje);

        List<Locacao> atrasadas = locacaoRepository.findLocacoesAtrasadas(hoje);
        atrasadas.addAll(locacaoRepository.findByStatusAtrasada());

        return atrasadas.stream()
                .distinct()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public void atualizarStatusAtrasadas(LocalDate dataReferencia) {
        List<Locacao> atrasadas = locacaoRepository.findLocacoesAtrasadas(dataReferencia);
        for (Locacao locacao : atrasadas) {
            locacao.setStatus(StatusLocacao.ATRASADA);
            locacaoRepository.save(locacao);
        }
    }

    @Transactional
    public LocacaoResponseDTO salvar(LocacaoCreateDTO locacaoDTO) {
        Cliente cliente = clienteRepository.findById(locacaoDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        List<Filme> filmes = new ArrayList<>();
        for (Long filmeId : locacaoDTO.getFilmesIds()) {
            Filme filme = filmeRepository.findById(filmeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Filme com ID " + filmeId + " não encontrado."));

            if (filme.getQuantidade_total() <= 0) {
                throw new BusinessException("Filme '" + filme.getTitulo() + "' não possui cópias disponíveis.");
            }
            filmes.add(filme);
        }

        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setData_locacao(LocalDate.now());
        locacao.setData_devolucao_prevista(LocalDate.now().plusDays(locacaoDTO.getDiasLocacao()));
        locacao.setStatus(StatusLocacao.ATIVA);

        List<LocacaoItem> itens = new ArrayList<>();
        for (Filme filme : filmes) {
            LocacaoItem item = new LocacaoItem();
            item.setLocacao(locacao);
            item.setFilme(filme);
            itens.add(item);

            filme.setQuantidade_total(filme.getQuantidade_total() - 1);
            filmeRepository.save(filme);
        }

        locacao.setItens(itens);
        Locacao locacaoSalva = locacaoRepository.save(locacao);

        return converterParaDTO(locacaoSalva);
    }

    @Transactional
    public LocacaoResponseDTO devolver(Long id) {
        Locacao locacao = buscarPorId(id);

        if (locacao.getStatus() == StatusLocacao.FINALIZADA) {
            throw new BusinessException("Esta locação já foi finalizada.");
        }

        locacao.setData_devolucao_real(LocalDate.now());
        locacao.setStatus(StatusLocacao.FINALIZADA);

        if (locacao.getItens() != null) {
            for (LocacaoItem item : locacao.getItens()) {
                Filme filme = item.getFilme();
                filme.setQuantidade_total(filme.getQuantidade_total() + 1);
                filmeRepository.save(filme);
            }
        }

        Locacao locacaoAtualizada = locacaoRepository.save(locacao);
        return converterParaDTO(locacaoAtualizada);
    }

    private LocacaoResponseDTO converterParaDTO(Locacao locacao) {
        LocacaoResponseDTO locacaoDTO = new LocacaoResponseDTO();

        locacaoDTO.setId(locacao.getId());
        locacaoDTO.setDataLocacao(locacao.getData_locacao());
        locacaoDTO.setDataDevolucaoPrevista(locacao.getData_devolucao_prevista());
        locacaoDTO.setDataDevolucaoReal(locacao.getData_devolucao_real());
        locacaoDTO.setStatus(locacao.getStatus());

        ClienteResponseDTO clienteDTO = new ClienteResponseDTO();
        clienteDTO.setId(locacao.getCliente().getId());
        clienteDTO.setNome(locacao.getCliente().getNome());
        clienteDTO.setEmail(locacao.getCliente().getEmail());
        locacaoDTO.setCliente(clienteDTO);

        if (locacao.getItens() != null) {
            List<FilmeResponseDTO> filmes = locacao.getItens().stream()
                    .map(item -> {
                        FilmeResponseDTO filmeDTO = new FilmeResponseDTO();
                        filmeDTO.setId(item.getFilme().getId());
                        filmeDTO.setTitulo(item.getFilme().getTitulo());
                        filmeDTO.setAno(item.getFilme().getAno());
                        filmeDTO.setQuantidade_total(item.getFilme().getQuantidade_total());
                        filmeDTO.setGenero(item.getFilme().getGenero().getNome());
                        return filmeDTO;
                    })
                    .toList();
            locacaoDTO.setFilmes(filmes);
        }

        return locacaoDTO;
    }
}
