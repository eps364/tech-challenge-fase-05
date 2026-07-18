/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.repository;

import br.com.fiap.susconnect.aps.infra.entity.TerritoryJpa;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerritoryJpaRepository extends JpaRepository<TerritoryJpa, UUID> {

  boolean existsByCode(String code);
}
