package com.ejemplo.pc1.Repository;

import com.ejemplo.pc1.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface userRepository extends JpaRepository<user,Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<user> findByUsername(String username);


}