package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Product;
import com.crm.ehelpdesk.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
