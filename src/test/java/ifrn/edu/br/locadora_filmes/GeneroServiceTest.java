package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ifrn.edu.br.locadora_filmes.service.GeneroService;
import ifrn.edu.br.locadora_filmes.repository.GeneroRepository;
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
