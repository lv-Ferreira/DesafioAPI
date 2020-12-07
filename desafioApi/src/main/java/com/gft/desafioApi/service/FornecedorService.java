package com.gft.desafioApi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.desafioApi.model.Fornecedor;
import com.gft.desafioApi.repository.FornecedorRepository;

@Service
public class FornecedorService {

	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	public Fornecedor atualizar(Long id, Fornecedor fornecedor) {
		Fornecedor fornecedorSalvo = this.fornecedorRepository.findById(id).orElse(null);
		BeanUtils.copyProperties(fornecedor, fornecedorSalvo, "codigo");
		return fornecedorRepository.save(fornecedor);
	}
}
