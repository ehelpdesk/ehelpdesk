package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Customer;
import com.crm.ehelpdesk.domain.ProductCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
