package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.DBAccessDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBAccessDetailsRepository extends JpaRepository<DBAccessDetails, Long> {

  public Long deleteByEntityId(Long entityId);
}
