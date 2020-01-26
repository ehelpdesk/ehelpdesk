package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.PersistentToken;
import com.crm.ehelpdesk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String> {

    List<PersistentToken> findByUser(User user);

    List<PersistentToken> findByTokenDateBefore(LocalDate localDate);
}
