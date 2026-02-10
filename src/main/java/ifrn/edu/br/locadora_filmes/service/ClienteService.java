package ifrn.edu.br.locadora_filmes.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ifrn.edu.br.locadora_filmes.repository.ClienteRepository;
import ifrn.edu.br.locadora_filmes.repository.LocacaoRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.ClienteCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.ClienteUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.ClienteResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.LocacaoResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.FilmeResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.ResourceNotFoundException;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Cliente;
import ifrn.edu.br.locadora_filmes.model.Locacao;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LocacaoRepository locacaoRepository;

    public List<ClienteResponseDTO> buscarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
    }

    public ClienteResponseDTO buscarPorIdDTO(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
        return converterParaDTO(cliente);
    }

    public List<LocacaoResponseDTO> buscarHistoricoLocacoes(Long clienteId) {
        Cliente cliente = buscarPorId(clienteId);
        List<Locacao> locacoes = locacaoRepository.findByCliente(cliente);
        return locacoes.stream()
                .map(this::converterLocacaoParaDTO)
                .toList();
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteCreateDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("Já existe um cliente com esse email.");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteDTO.getNome());
        cliente.setEmail(clienteDTO.getEmail());

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return converterParaDTO(clienteSalvo);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO clienteDTO) {
        Cliente clienteExistente = buscarPorId(id);

        if (!clienteExistente.getEmail().equals(clienteDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("Já existe um cliente com esse email.");
        }

        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);

        return converterParaDTO(clienteAtualizado);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);

        if (locacaoRepository.existsByClienteIdAndStatus(id, StatusLocacao.ATIVA) ||
                locacaoRepository.existsByClienteIdAndStatus(id, StatusLocacao.ATRASADA)) {
            throw new BusinessException("Não é possível deletar um cliente com locações ativas ou atrasadas.");
        }

        clienteRepository.delete(cliente);
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        ClienteResponseDTO clienteDTO = new ClienteResponseDTO();

        clienteDTO.setId(cliente.getId());
        clienteDTO.setNome(cliente.getNome());
        clienteDTO.setEmail(cliente.getEmail());

        return clienteDTO;
    }

    private LocacaoResponseDTO converterLocacaoParaDTO(Locacao locacao) {
        LocacaoResponseDTO locacaoDTO = new LocacaoResponseDTO();

        locacaoDTO.setId(locacao.getId());
        locacaoDTO.setCliente(converterParaDTO(locacao.getCliente()));
        locacaoDTO.setDataLocacao(locacao.getData_locacao());
        locacaoDTO.setDataDevolucaoPrevista(locacao.getData_devolucao_prevista());
        locacaoDTO.setDataDevolucaoReal(locacao.getData_devolucao_real());
        locacaoDTO.setStatus(locacao.getStatus());

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
