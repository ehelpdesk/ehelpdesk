package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByLogin(String login);

}
