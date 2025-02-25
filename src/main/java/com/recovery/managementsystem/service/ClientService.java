package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Client;
import com.recovery.managementsystem.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;

	public List<Client> findAll() {
		
		return clientRepository.findAll();
	}

	public void saveclient(Client client) {
		clientRepository.save(client);
		
	}

	public long getTotalClients() {
		return clientRepository.count();
	}

}
