package ifrn.edu.br.locadora_filmes.service;

import ifrn.edu.br.locadora_filmes.dto.requests.LoginRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.RegisterRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.AuthResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.UsuarioResponseDTO;
import ifrn.edu.br.locadora_filmes.exception.BusinessException;
import ifrn.edu.br.locadora_filmes.model.Usuario;
import ifrn.edu.br.locadora_filmes.repository.UsuarioRepository;
import ifrn.edu.br.locadora_filmes.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe um usuário com esse email.");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .build();

        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        String token = jwtService.generateToken(usuarioSalvo);

        return buildAuthResponse(usuarioSalvo, token);
    }

    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()));
        } catch (BadCredentialsException e) {
            throw new BusinessException("Email ou senha inválidos.");
        }

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado."));

        String token = jwtService.generateToken(usuario);

        return buildAuthResponse(usuario, token);
    }

    private AuthResponseDTO buildAuthResponse(Usuario usuario, String token) {
        UsuarioResponseDTO usuarioResponse = UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .expiresIn(jwtExpiration)
                .usuario(usuarioResponse)
                .build();
    }
}
