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
import com.gft.desafioApi.model.Produto;
import com.gft.desafioApi.repository.ProdutoRepository;
import com.gft.desafioApi.service.ProdutoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Produtos")
@RestController
@RequestMapping("/api/produtos")
public class ProdutoResource {

	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todos os Produtos")
	@GetMapping
	public List<Produto> listar() {
		return produtoRepository.findAll();
	}
	
	@ApiOperation("Inserir um Novo Produto")
	@PostMapping
	public ResponseEntity<Produto> criar(@Valid @RequestBody Produto produto, HttpServletResponse response) {
		Produto produtoSalvo = produtoRepository.save(produto);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, produtoSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
	}
	
	@ApiOperation("Atualizar um Produto")
	@PutMapping("/{id}")
	public ResponseEntity<Produto> atualizar(@PathVariable Long id, @Valid @RequestBody Produto produto){
		Produto produtoSalvo = produtoService.atualizar(id, produto);	
		return ResponseEntity.ok(produtoSalvo);
	}
	
	@ApiOperation("Buscar um Produto em Específico")
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPeloCodigo(@PathVariable Long id) {
		Produto produto = this.produtoRepository.findById(id).orElse(null);
		return produto != null ? ResponseEntity.ok(produto) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Buscar Produto pelo Nome")
	@GetMapping("/nome/{nome}")
	public ResponseEntity<Produto> buscarPeloNome(@PathVariable String nome) {
		Produto peloNome =  this.produtoRepository.findByNome(nome);
		return peloNome != null ? ResponseEntity.ok(peloNome) : ResponseEntity.notFound().build();
	}
	
	@ApiOperation("Listar os Produtos em ordem crescente pelo Nome")
	@GetMapping("/asc")
	public List<Produto> buscarPeloNomeAsc() {
		return this.produtoRepository.findAll(Sort.by(Direction.ASC, "nome"));
	}
	
	@ApiOperation("Listar os Produtos em ordem decrescente pelo Nome")
	@GetMapping("/desc")
	public List<Produto> buscarPeloNomeDesc() {
		return this.produtoRepository.findAll(Sort.by(Direction.DESC, "nome"));
	}
	
	@ApiOperation("Remover um Produto")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		this.produtoRepository.deleteById(id);
	}
}
