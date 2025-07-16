package com.synechron.sandboxmanagement.repository;

import com.synechron.sandboxmanagement.entity.Sandbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SandboxRepository extends JpaRepository<Sandbox, Long> {
    Optional<Sandbox> findByName(String name);
    Optional<Sandbox> findByNamespace(String namespace);
    boolean existsByName(String name);
    boolean existsByNamespace(String namespace);
}
