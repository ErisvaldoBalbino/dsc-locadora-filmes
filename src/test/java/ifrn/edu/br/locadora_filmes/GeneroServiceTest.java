package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.GeneroUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.GeneroResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Genero;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class GeneroServiceTests {

	@InjectMocks
	private GeneroService generoService;

	@Mock
	private GeneroRepository generoRepository;

	@Test
	void testSalvarGenero_ShouldPass() {
		GeneroCreateDTO generoDTO = new GeneroCreateDTO();
		generoDTO.setNome("Ação");

		Genero generoSalvo = new Genero();
		generoSalvo.setId(1L);
		generoSalvo.setNome("Ação");

		Mockito.when(generoRepository.existsByNome("Ação")).thenReturn(false);
		Mockito.when(generoRepository.save(Mockito.any(Genero.class))).thenReturn(generoSalvo);

		GeneroResponseDTO resultado = generoService.salvar(generoDTO);

		assertNotNull(resultado);
		assertEquals("Ação", resultado.getNome());
	}

	@Test
	void testAtualizarGenero_ShouldPass() {
		long generoId = 1L;
		GeneroUpdateDTO generoDTO = new GeneroUpdateDTO();
		generoDTO.setNome("Ação");

		Genero generoSalvo = new Genero();
		generoSalvo.setId(1L);
		generoSalvo.setNome("Ação");

		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.of(generoSalvo));
		Mockito.when(generoRepository.save(Mockito.any(Genero.class))).thenReturn(generoSalvo);

		GeneroResponseDTO resultado = generoService.atualizar(generoId, generoDTO);

		assertNotNull(resultado);
		assertEquals("Ação", resultado.getNome());
	}

	@Test
	void testAtualizarGenero_ShouldFail_NomeJaExistente() {
		long generoId = 1L;
		GeneroUpdateDTO generoDTO = new GeneroUpdateDTO();
		generoDTO.setNome("Ação");

		Genero generoSalvo = new Genero();
		generoSalvo.setId(1L);
		generoSalvo.setNome("Comédia");

		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.of(generoSalvo));
		Mockito.when(generoRepository.existsByNome("Ação")).thenReturn(true);

		assertThrows(RuntimeException.class, () -> generoService.atualizar(generoId, generoDTO));
	}

	@Test
	void testSalvarGenero_ShouldFail_NomeJaExistente() {
		GeneroCreateDTO generoDTO = new GeneroCreateDTO();
		generoDTO.setNome("Ação");

		Mockito.when(generoRepository.existsByNome("Ação")).thenReturn(true);

		assertThrows(RuntimeException.class, () -> generoService.salvar(generoDTO));
	}

	@Test
	void testBuscarPorId_ShouldPass() {
		long generoId = 1L;
		Genero mockGenero = new Genero();
		mockGenero.setId(generoId);
		mockGenero.setNome("Ação");

		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.of(mockGenero));

		Genero resultdo = generoService.buscarPorId(generoId);

		assertNotNull(resultdo);
		assertEquals(generoId, resultdo.getId());
		assertEquals("Ação", resultdo.getNome());
	
	}

	@Test
	void testBuscarTodos_ShouldPass() {
		Genero genero = new Genero();
		genero.setId(1L);
		genero.setNome("Ação");

		Mockito.when(generoRepository.findAll()).thenReturn(List.of(genero));

		List<GeneroResponseDTO> resultado = generoService.buscarTodos();

		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Ação", resultado.get(0).getNome());
	}

	@Test
	void testBuscarPorId_ShouldFail_GeneroNaoEncontrado() {
		long generoId = 1L;
		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> generoService.buscarPorId(generoId));
	}

	@Test
	void testDeletar_ShouldPass() {
		long generoId = 1L;
		Genero genero = new Genero();
		genero.setId(generoId);
		genero.setNome("Ação");

		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.of(genero));
		Mockito.doNothing().when(generoRepository).delete(genero);

		generoService.deletar(generoId);

		Mockito.verify(generoRepository, Mockito.times(1)).delete(genero);
	}

	@Test
	void testDeletar_ShouldFail_GeneroNaoEncontrado() {
		long generoId = 999L;
		Mockito.when(generoRepository.findById(generoId)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> generoService.deletar(generoId));
	}

}
