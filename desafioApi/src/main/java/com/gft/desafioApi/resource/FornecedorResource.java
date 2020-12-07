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
import com.gft.desafioApi.model.Fornecedor;
import com.gft.desafioApi.repository.FornecedorRepository;
import com.gft.desafioApi.service.FornecedorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Fornecedores")
@RestController
@RequestMapping("/api/fornecedores")
public class FornecedorResource {

	@Autowired
	FornecedorRepository fornecedorRepository;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todos os Fornecedores")
	@GetMapping
	public List<Fornecedor> listar() {
		return fornecedorRepository.findAll();
	}
	
	@ApiOperation("Inserir um Novo Fornecedor")
	@PostMapping
	public ResponseEntity<Fornecedor> criar(@Valid @RequestBody Fornecedor fornecedor, HttpServletResponse response) {
		Fornecedor fornecedorSalvo = fornecedorRepository.save(fornecedor);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, fornecedorSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(fornecedorSalvo);
	}
	
	@ApiOperation("Atualizar um Fornecedor")
	@PutMapping("/{id}")
	public ResponseEntity<Fornecedor> atualizar(@PathVariable Long id, @Valid @RequestBody Fornecedor fornecedor){
		Fornecedor fornecedorSalvo = fornecedorService.atualizar(id, fornecedor);	
		return ResponseEntity.ok(fornecedorSalvo);
	}
	
	@ApiOperation("Buscar um Fornecedor em Específico")
	@GetMapping("/{id}")
	public ResponseEntity<Fornecedor> buscarPeloCodigo(@PathVariable Long id) {
		Fornecedor fornecedor = this.fornecedorRepository.findById(id).orElse(null);
		return fornecedor != null ? ResponseEntity.ok(fornecedor) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Buscar Fornecedor pelo Nome")
	@GetMapping("/nome/{nome}")
	public ResponseEntity<Fornecedor> buscarPeloNome(@PathVariable String nome) {
		Fornecedor peloNome =  this.fornecedorRepository.findByNome(nome);
		return peloNome != null ? ResponseEntity.ok(peloNome) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Listar os Fornecedores em ordem crescente pelo Nome")
	@GetMapping("/asc")
	public List<Fornecedor> buscarPeloNomeAsc() {
		return this.fornecedorRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
	
	@ApiOperation("Listar os Fornecedores em ordem decrescente pelo Nome")
	@GetMapping("/desc")
	public List<Fornecedor> buscarPeloNomeDesc() {
		return this.fornecedorRepository.findAll(Sort.by(Direction.DESC, "nome"));
	}
	
	@ApiOperation("Remover um Fornecedor")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		this.fornecedorRepository.deleteById(id);
	}
}
