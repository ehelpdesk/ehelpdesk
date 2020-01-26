package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
  List<Authority> findByNameNotIn(Set<String> names);
}
