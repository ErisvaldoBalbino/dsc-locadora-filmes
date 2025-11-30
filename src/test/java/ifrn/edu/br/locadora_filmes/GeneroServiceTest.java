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
import ifrn.edu.br.locadora_filmes.dto.responses.GeneroResponseDTO;
import ifrn.edu.br.locadora_filmes.model.Genero;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class GeneroServiceTests {

	@InjectMocks
	private GeneroService generoService;

	@Mock
	private GeneroRepository generoRepository;

	@Test
	void testCreateGenero() {
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
	void testGetGeneroById() {
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

}
