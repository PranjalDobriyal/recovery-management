package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
