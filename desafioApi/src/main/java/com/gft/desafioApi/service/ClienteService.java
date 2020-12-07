package com.gft.desafioApi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gft.desafioApi.model.Cliente;
import com.gft.desafioApi.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	public Cliente atualizar(Long id, Cliente cliente) {
		Cliente clienteSalvo = this.clienteRepository.findById(id).orElse(null);
		BeanUtils.copyProperties(cliente, clienteSalvo, "codigo");
		return clienteRepository.save(cliente);
	}
}
