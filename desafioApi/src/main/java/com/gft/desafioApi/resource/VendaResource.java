package com.gft.desafioApi.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.gft.desafioApi.model.Venda;
import com.gft.desafioApi.repository.VendaRepository;
import com.gft.desafioApi.service.VendaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Vendas")
@RestController
@RequestMapping("/api/vendas")
public class VendaResource {

	@Autowired
	VendaRepository vendaRepository;
	
	@Autowired
	private VendaService vendaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@ApiOperation("Listar todas as Vendas")
	@GetMapping
	public List<Venda> listar() {
		return vendaRepository.findAll();
	}
	
	@ApiOperation("Inserir uma Nova Venda")
	@PostMapping
	public ResponseEntity<Venda> criar(@Valid @RequestBody Venda venda, HttpServletResponse response) {
		Venda vendaSalvo = vendaRepository.save(venda);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, vendaSalvo.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(vendaSalvo);
	}
	
	@ApiOperation("Atualizar uma Venda")
	@PutMapping("/{id}")
	public ResponseEntity<Venda> atualizar(@PathVariable Long id, @Valid @RequestBody Venda venda){
		Venda vendaSalvo = vendaService.atualizar(id, venda);	
		return ResponseEntity.ok(vendaSalvo);
	}
	
	@ApiOperation("Buscar uma Venda em Específico")
	@GetMapping("/{id}")
	public ResponseEntity<Venda> buscarPeloCodigo(@PathVariable Long id) {
		Venda venda = this.vendaRepository.findById(id).orElse(null);
		return venda != null ? ResponseEntity.ok(venda) : ResponseEntity.notFound().build();
	}
	
	/*
	 * @ApiOperation("Buscar Produto pelo Nome")
	 * 
	 * @GetMapping("/nome/{nome}") public ResponseEntity<Produto>
	 * buscarPeloNome(@PathVariable String nome) { Produto peloNome =
	 * this.produtoRepository.findByNome(nome); return peloNome != null ?
	 * ResponseEntity.ok(peloNome) : ResponseEntity.notFound().build(); }
	 */
	
	/*
	 * @ApiOperation("Listar os Produtos em ordem crescente pelo Nome")
	 * 
	 * @GetMapping("/asc") public List<Produto> buscarPeloNomeAsc() { return
	 * this.produtoRepository.findAll(Sort.by(Direction.ASC, "nome")); }
	 * 
	 * @ApiOperation("Listar os Produtos em ordem decrescente pelo Nome")
	 * 
	 * @GetMapping("/desc") public List<Produto> buscarPeloNomeDesc() { return
	 * this.produtoRepository.findAll(Sort.by(Direction.DESC, "nome")); }
	 */
	
	@ApiOperation("Remover uma Venda")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {
		this.vendaRepository.deleteById(id);
	}
}
