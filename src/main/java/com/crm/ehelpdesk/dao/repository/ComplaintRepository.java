package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
}
