package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ifrn.edu.br.locadora_filmes.service.ClienteService;
import ifrn.edu.br.locadora_filmes.repository.ClienteRepository;
import ifrn.edu.br.locadora_filmes.repository.LocacaoRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.ClienteCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.ClienteUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.ClienteResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.ResourceNotFoundException;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Cliente;
import ifrn.edu.br.locadora_filmes.model.StatusLocacao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private LocacaoRepository locacaoRepository;

    @Test
    void testSalvarCliente_ShouldPass() {
        ClienteCreateDTO clienteDTO = new ClienteCreateDTO();
        clienteDTO.setNome("João Silva");
        clienteDTO.setEmail("joao@email.com");

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(1L);
        clienteSalvo.setNome("João Silva");
        clienteSalvo.setEmail("joao@email.com");

        Mockito.when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(false);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        ClienteResponseDTO resultado = clienteService.salvar(clienteDTO);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
    }

    @Test
    void testSalvarCliente_ShouldFail_EmailJaExistente() {
        ClienteCreateDTO clienteDTO = new ClienteCreateDTO();
        clienteDTO.setNome("João Silva");
        clienteDTO.setEmail("joao@email.com");

        Mockito.when(clienteRepository.existsByEmail("joao@email.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> clienteService.salvar(clienteDTO));
    }

    @Test
    void testAtualizarCliente_ShouldPass() {
        long clienteId = 1L;

        ClienteUpdateDTO clienteDTO = new ClienteUpdateDTO();
        clienteDTO.setNome("João Atualizado");
        clienteDTO.setEmail("joao@email.com");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(clienteId);
        clienteExistente.setNome("João Silva");
        clienteExistente.setEmail("joao@email.com");

        Cliente clienteSalvo = new Cliente();
        clienteSalvo.setId(clienteId);
        clienteSalvo.setNome("João Atualizado");
        clienteSalvo.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteSalvo);

        ClienteResponseDTO resultado = clienteService.atualizar(clienteId, clienteDTO);

        assertNotNull(resultado);
        assertEquals("João Atualizado", resultado.getNome());
    }

    @Test
    void testAtualizarCliente_ShouldFail_EmailJaExistente() {
        long clienteId = 1L;

        ClienteUpdateDTO clienteDTO = new ClienteUpdateDTO();
        clienteDTO.setNome("João");
        clienteDTO.setEmail("outro@email.com");

        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(clienteId);
        clienteExistente.setNome("João");
        clienteExistente.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(clienteExistente));
        Mockito.when(clienteRepository.existsByEmail("outro@email.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> clienteService.atualizar(clienteId, clienteDTO));
    }

    @Test
    void testAtualizarCliente_ShouldFail_ClienteNaoEncontrado() {
        long clienteId = 999L;

        ClienteUpdateDTO clienteDTO = new ClienteUpdateDTO();
        clienteDTO.setNome("João");
        clienteDTO.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.atualizar(clienteId, clienteDTO));
    }

    @Test
    void testBuscarPorId_ShouldPass() {
        long clienteId = 1L;

        Cliente mockCliente = new Cliente();
        mockCliente.setId(clienteId);
        mockCliente.setNome("João Silva");
        mockCliente.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(mockCliente));

        Cliente resultado = clienteService.buscarPorId(clienteId);

        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
    }

    @Test
    void testBuscarPorId_ShouldFail_ClienteNaoEncontrado() {
        long clienteId = 999L;
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.buscarPorId(clienteId));
    }

    @Test
    void testBuscarTodos_ShouldPass() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNome("João");
        cliente1.setEmail("joao@email.com");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2L);
        cliente2.setNome("Maria");
        cliente2.setEmail("maria@email.com");

        Mockito.when(clienteRepository.findAll()).thenReturn(List.of(cliente1, cliente2));

        List<ClienteResponseDTO> resultado = clienteService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("João", resultado.get(0).getNome());
        assertEquals("Maria", resultado.get(1).getNome());
    }

    @Test
    void testBuscarTodos_ShouldReturnEmptyList() {
        Mockito.when(clienteRepository.findAll()).thenReturn(List.of());

        List<ClienteResponseDTO> resultado = clienteService.buscarTodos();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void testDeletar_ShouldPass() {
        long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(locacaoRepository.existsByClienteIdAndStatus(clienteId, StatusLocacao.ATIVA)).thenReturn(false);
        Mockito.when(locacaoRepository.existsByClienteIdAndStatus(clienteId, StatusLocacao.ATRASADA)).thenReturn(false);
        Mockito.doNothing().when(clienteRepository).delete(cliente);

        clienteService.deletar(clienteId);

        Mockito.verify(clienteRepository, Mockito.times(1)).delete(cliente);
    }

    @Test
    void testDeletar_ShouldFail_ClienteNaoEncontrado() {
        long clienteId = 999L;
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.deletar(clienteId));
    }

    @Test
    void testDeletar_ShouldFail_ClienteComLocacoesAtivas() {
        long clienteId = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("João");
        cliente.setEmail("joao@email.com");

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(locacaoRepository.existsByClienteIdAndStatus(clienteId, StatusLocacao.ATIVA)).thenReturn(true);

        assertThrows(BusinessException.class, () -> clienteService.deletar(clienteId));
    }
}
