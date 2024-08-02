package org.sid.documentationservice.repository;

import org.sid.documentationservice.entity.Documentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentationRepository extends JpaRepository<Documentation, Long> {
    Optional<Documentation> findByName(String name);
}
