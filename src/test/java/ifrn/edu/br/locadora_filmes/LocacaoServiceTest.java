package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ifrn.edu.br.locadora_filmes.service.LocacaoService;
import ifrn.edu.br.locadora_filmes.repository.ClienteRepository;
import ifrn.edu.br.locadora_filmes.repository.FilmeRepository;
import ifrn.edu.br.locadora_filmes.repository.LocacaoRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.LocacaoCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.LocacaoResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.ResourceNotFoundException;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Cliente;
import ifrn.edu.br.locadora_filmes.model.Filme;
import ifrn.edu.br.locadora_filmes.model.Genero;
import ifrn.edu.br.locadora_filmes.model.Locacao;
import ifrn.edu.br.locadora_filmes.model.LocacaoItem;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LocacaoServiceTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoRepository locacaoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private FilmeRepository filmeRepository;

    private Genero criarGenero() {
        Genero genero = new Genero();
        genero.setId(1L);
        genero.setNome("Ação");
        return genero;
    }

    private Cliente criarCliente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setEmail("joao@email.com");
        return cliente;
    }

    private Filme criarFilme(Long id, String titulo, int estoque) {
        Filme filme = new Filme();
        filme.setId(id);
        filme.setTitulo(titulo);
        filme.setAno(2025);
        filme.setQuantidade_total(estoque);
        filme.setGenero(criarGenero());
        return filme;
    }

    private Locacao criarLocacao(StatusLocacao status) {
        Cliente cliente = criarCliente();
        Filme filme = criarFilme(1L, "Filme 1", 5);

        Locacao locacao = new Locacao();
        locacao.setId(1L);
        locacao.setCliente(cliente);
        locacao.setData_locacao(LocalDate.now());
        locacao.setData_devolucao_prevista(LocalDate.now().plusDays(7));
        locacao.setStatus(status);

        LocacaoItem item = new LocacaoItem();
        item.setId(1L);
        item.setLocacao(locacao);
        item.setFilme(filme);
        locacao.setItens(List.of(item));

        return locacao;
    }

    @Test
    void testSalvarLocacao_ShouldPass() {
        Cliente cliente = criarCliente();
        Filme filme = criarFilme(1L, "Filme 1", 5);

        LocacaoCreateDTO locacaoDTO = new LocacaoCreateDTO();
        locacaoDTO.setClienteId(1L);
        locacaoDTO.setDiasLocacao(7);
        locacaoDTO.setFilmesIds(List.of(1L));

        Locacao locacaoSalva = criarLocacao(StatusLocacao.ATIVA);

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Mockito.when(filmeRepository.findById(1L)).thenReturn(Optional.of(filme));
        Mockito.when(filmeRepository.save(Mockito.any(Filme.class))).thenReturn(filme);
        Mockito.when(locacaoRepository.save(Mockito.any(Locacao.class))).thenReturn(locacaoSalva);

        LocacaoResponseDTO resultado = locacaoService.salvar(locacaoDTO);

        assertNotNull(resultado);
        assertEquals(StatusLocacao.ATIVA, resultado.getStatus());
        assertEquals("João Silva", resultado.getCliente().getNome());
    }

    @Test
    void testSalvarLocacao_ShouldFail_ClienteNaoEncontrado() {
        LocacaoCreateDTO locacaoDTO = new LocacaoCreateDTO();
        locacaoDTO.setClienteId(999L);
        locacaoDTO.setDiasLocacao(7);
        locacaoDTO.setFilmesIds(List.of(1L));

        Mockito.when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locacaoService.salvar(locacaoDTO));
    }

    @Test
    void testSalvarLocacao_ShouldFail_FilmeNaoEncontrado() {
        Cliente cliente = criarCliente();

        LocacaoCreateDTO locacaoDTO = new LocacaoCreateDTO();
        locacaoDTO.setClienteId(1L);
        locacaoDTO.setDiasLocacao(7);
        locacaoDTO.setFilmesIds(List.of(999L));

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Mockito.when(filmeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locacaoService.salvar(locacaoDTO));
    }

    @Test
    void testSalvarLocacao_ShouldFail_FilmeSemEstoque() {
        Cliente cliente = criarCliente();
        Filme filmeSemEstoque = criarFilme(1L, "Filme Esgotado", 0);

        LocacaoCreateDTO locacaoDTO = new LocacaoCreateDTO();
        locacaoDTO.setClienteId(1L);
        locacaoDTO.setDiasLocacao(7);
        locacaoDTO.setFilmesIds(List.of(1L));

        Mockito.when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Mockito.when(filmeRepository.findById(1L)).thenReturn(Optional.of(filmeSemEstoque));

        assertThrows(BusinessException.class, () -> locacaoService.salvar(locacaoDTO));
    }

    @Test
    void testDevolver_ShouldPass() {
        Locacao locacao = criarLocacao(StatusLocacao.ATIVA);

        Locacao locacaoDevolvida = criarLocacao(StatusLocacao.FINALIZADA);
        locacaoDevolvida.setData_devolucao_real(LocalDate.now());

        Mockito.when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));
        Mockito.when(filmeRepository.save(Mockito.any(Filme.class))).thenReturn(locacao.getItens().get(0).getFilme());
        Mockito.when(locacaoRepository.save(Mockito.any(Locacao.class))).thenReturn(locacaoDevolvida);

        LocacaoResponseDTO resultado = locacaoService.devolver(1L);

        assertNotNull(resultado);
        assertEquals(StatusLocacao.FINALIZADA, resultado.getStatus());
    }

    @Test
    void testDevolver_ShouldFail_LocacaoJaFinalizada() {
        Locacao locacao = criarLocacao(StatusLocacao.FINALIZADA);

        Mockito.when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));

        assertThrows(BusinessException.class, () -> locacaoService.devolver(1L));
    }

    @Test
    void testDevolver_ShouldFail_LocacaoNaoEncontrada() {
        Mockito.when(locacaoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> locacaoService.devolver(999L));
    }

    @Test
    void testBuscarPorId_ShouldPass() {
        Locacao locacao = criarLocacao(StatusLocacao.ATIVA);

        Mockito.when(locacaoRepository.findById(1L)).thenReturn(Optional.of(locacao));

        Locacao resultado = locacaoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(StatusLocacao.ATIVA, resultado.getStatus());
    }

    @Test
    void testBuscarPorId_ShouldFail_LocacaoNaoEncontrada() {
        Mockito.when(locacaoRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> locacaoService.buscarPorId(999L));
    }

    @Test
    void testBuscarTodos_ShouldPass() {
        Locacao locacao1 = criarLocacao(StatusLocacao.ATIVA);
        Locacao locacao2 = criarLocacao(StatusLocacao.FINALIZADA);
        locacao2.setId(2L);

        Mockito.when(locacaoRepository.findAll()).thenReturn(List.of(locacao1, locacao2));

        List<LocacaoResponseDTO> resultado = locacaoService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void testBuscarTodos_ShouldReturnEmptyList() {
        Mockito.when(locacaoRepository.findAll()).thenReturn(List.of());

        List<LocacaoResponseDTO> resultado = locacaoService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
}
