package com.gft.desafioApi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.desafioApi.model.Venda;
import com.gft.desafioApi.repository.VendaRepository;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;
	
	public Venda atualizar(Long id, Venda venda) {
		Venda vendaSalvo = this.vendaRepository.findById(id).orElse(null);
		BeanUtils.copyProperties(venda, vendaSalvo, "codigo");
		return vendaRepository.save(venda);
	}
}
