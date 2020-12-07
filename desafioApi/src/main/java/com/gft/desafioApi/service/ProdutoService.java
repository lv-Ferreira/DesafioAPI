package com.gft.desafioApi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.desafioApi.model.Produto;
import com.gft.desafioApi.repository.ProdutoRepository;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	public Produto atualizar(Long id, Produto produto) {
		Produto produtoSalvo = this.produtoRepository.findById(id).orElse(null);
		BeanUtils.copyProperties(produto, produtoSalvo, "codigo");
		return produtoRepository.save(produto);
	}
}
