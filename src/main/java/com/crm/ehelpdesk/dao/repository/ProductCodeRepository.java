package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Authority;
import com.crm.ehelpdesk.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCodeRepository extends JpaRepository<ProductCode, Long> {
    Optional<ProductCode> findByCodeAndUsed(long code, boolean used);
}
