package ifrn.edu.br.locadora_filmes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import ifrn.edu.br.locadora_filmes.service.AuthService;
import ifrn.edu.br.locadora_filmes.repository.UsuarioRepository;
import ifrn.edu.br.locadora_filmes.security.JwtService;
import ifrn.edu.br.locadora_filmes.dto.requests.LoginRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.RegisterRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.AuthResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Usuario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testRegister_ShouldPass() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNome("João");
        request.setEmail("joao@email.com");
        request.setSenha("123456");

        Usuario usuarioSalvo = Usuario.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .senha("encoded")
                .build();

        Mockito.when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("123456")).thenReturn("encoded");
        Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioSalvo);
        Mockito.when(jwtService.generateToken(usuarioSalvo)).thenReturn("jwt-token");

        AuthResponseDTO resultado = authService.register(request);

        assertNotNull(resultado);
        assertEquals("jwt-token", resultado.getToken());
        assertEquals("Bearer", resultado.getTipo());
        assertEquals("João", resultado.getUsuario().getNome());
        assertEquals("joao@email.com", resultado.getUsuario().getEmail());
    }

    @Test
    void testRegister_ShouldFail_EmailJaExistente() {
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setNome("João");
        request.setEmail("joao@email.com");
        request.setSenha("123456");

        Mockito.when(usuarioRepository.existsByEmail("joao@email.com")).thenReturn(true);

        assertThrows(BusinessException.class, () -> authService.register(request));
    }

    @Test
    void testLogin_ShouldPass() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("joao@email.com");
        request.setSenha("123456");

        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .senha("encoded")
                .build();

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("joao@email.com", "123456"));
        Mockito.when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));
        Mockito.when(jwtService.generateToken(usuario)).thenReturn("jwt-token");

        AuthResponseDTO resultado = authService.login(request);

        assertNotNull(resultado);
        assertEquals("jwt-token", resultado.getToken());
        assertEquals("João", resultado.getUsuario().getNome());
    }

    @Test
    void testLogin_ShouldFail_CredenciaisInvalidas() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("joao@email.com");
        request.setSenha("senhaerrada");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BusinessException.class, () -> authService.login(request));
    }
}
