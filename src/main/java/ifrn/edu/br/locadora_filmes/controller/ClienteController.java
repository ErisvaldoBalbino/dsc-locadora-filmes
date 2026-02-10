package ifrn.edu.br.locadora_filmes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import ifrn.edu.br.locadora_filmes.dto.requests.ClienteCreateDTO;
import ifrn.edu.br.locadora_filmes.dto.requests.ClienteUpdateDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.ClienteResponseDTO;
import ifrn.edu.br.locadora_filmes.dto.responses.LocacaoResponseDTO;
import ifrn.edu.br.locadora_filmes.service.ClienteService;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados", responses = @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"))
    public ResponseEntity<List<ClienteResponseDTO>> buscarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os detalhes de um cliente específico", responses = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Cliente não encontrado.\"}")))
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarPorIdDTO(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("{id}/locacoes")
    @Operation(summary = "Histórico de locações", description = "Retorna todas as locações (ativas, finalizadas e atrasadas) de um cliente", responses = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<List<LocacaoResponseDTO>> buscarHistoricoLocacoes(
            @Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        List<LocacaoResponseDTO> locacoes = clienteService.buscarHistoricoLocacoes(id);
        return ResponseEntity.ok(locacoes);
    }

    @PostMapping()
    @Operation(summary = "Criar um novo cliente", description = "Cria um novo cliente. O email deve ser único.", responses = {
            @ApiResponse(responseCode = "200", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado ou dados inválidos", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Já existe um cliente com esse email.\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido")
    })
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteCreateDTO clienteDTO) {
        ClienteResponseDTO cliente = clienteService.salvar(clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um cliente", description = "Atualiza os dados de um cliente existente", responses = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Email já em uso por outro cliente"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @Parameter(description = "ID do cliente", example = "1") @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateDTO clienteDTO) {
        ClienteResponseDTO cliente = clienteService.atualizar(id, clienteDTO);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar um cliente", description = "Deleta um cliente. Não é possível deletar clientes com locações ativas ou atrasadas.", responses = {
            @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Cliente possui locações ativas ou atrasadas", content = @Content(examples = @ExampleObject(value = "{\"timestamp\": \"2026-02-09T20:00:00\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Não é possível deletar um cliente com locações ativas ou atrasadas.\"}"))),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do cliente", example = "1") @PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
