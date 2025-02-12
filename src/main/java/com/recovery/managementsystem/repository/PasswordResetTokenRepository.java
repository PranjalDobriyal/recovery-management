package com.recovery.managementsystem.repository;

import com.recovery.managementsystem.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    Optional<PasswordResetToken> findByToken(String token);

    
  
    PasswordResetToken findByEmployeeId(Integer id);

}