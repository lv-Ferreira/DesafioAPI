package com.gft.desafioApi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gft.desafioApi.event.RecursoCriadoEvent;
import com.gft.desafioApi.model.Cliente;
import com.gft.desafioApi.repository.ClienteRepository;
import com.gft.desafioApi.service.ClienteService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Clientes")
@RestController
@RequestMapping("/api/clientes")
public class ClienteResource {

	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todos os Clientes")
	@GetMapping
	public List<Cliente> listar() {
		return clienteRepository.findAll();
	}
	
	@ApiOperation("Inserir um Novo Cliente")
	@PostMapping
	public ResponseEntity<Cliente> criar(@Valid @RequestBody Cliente cliente, HttpServletResponse response) {
		Cliente clienteSalvo = clienteRepository.save(cliente);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, clienteSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSalvo);
	}
	
	@ApiOperation("Atualizar um Cliente")
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente){
		Cliente clienteSalvo = clienteService.atualizar(id, cliente);	
		return ResponseEntity.ok(clienteSalvo);
	}
	
	@ApiOperation("Buscar Cliente pelo ID")
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPeloCodigo(@PathVariable Long id) {
		Cliente cliente = this.clienteRepository.findById(id).orElse(null);
		return cliente != null ? ResponseEntity.ok(cliente) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Buscar Cliente pelo Nome")
	@GetMapping("/nome/{nome}")
	public ResponseEntity<Cliente> buscarPeloNome(@PathVariable String nome) {
		Cliente peloNome =  this.clienteRepository.findByNome(nome);
		return peloNome != null ? ResponseEntity.ok(peloNome) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Listar os Clientes em ordem crescente pelo Nome")
	@GetMapping("/asc")
	public List<Cliente> buscarPeloNomeAsc() {
		return this.clienteRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
	
	@ApiOperation("Listar os Clientes em ordem decrescente pelo Nome")
	@GetMapping("/desc")
	public List<Cliente> buscarPeloNomeDesc() {
		return this.clienteRepository.findAll(Sort.by(Direction.DESC, "nome"));
	}
	
	@ApiOperation("Remover um Cliente")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		this.clienteRepository.deleteById(id);
	}
	
}
