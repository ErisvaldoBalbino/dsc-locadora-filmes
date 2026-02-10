package ifrn.edu.br.locadora_filmes.controller;

import ifrn.edu.br.locadora_filmes.dto.requests.LoginRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.RegisterRequestDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.AuthResponseDTO;
import ifrn.edu.br.locadora_filmes.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação e registro")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema e retorna um token JWT para autenticação", responses = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Já existe um usuário com esse email.\"}")))
    })
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        AuthResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Autentica com email e senha, retorna token JWT válido por 24h", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Email ou senha inválidos", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Email ou senha inválidos.\"}")))
    })
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        AuthResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
