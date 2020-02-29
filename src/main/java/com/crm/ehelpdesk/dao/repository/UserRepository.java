package com.crm.ehelpdesk.dao.repository;

import com.crm.ehelpdesk.domain.Authority;
import com.crm.ehelpdesk.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.crm.ehelpdesk.config.constants.CacheConstants.USERS_BY_EMAIL_CACHE;
import static com.crm.ehelpdesk.config.constants.CacheConstants.USERS_BY_LOGIN_CACHE;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByActivationKey(String activationKey);

  List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

  Optional<User> findOneByResetKey(String resetKey);

  Optional<User> findOneByEmailIgnoreCase(String email);

  Optional<User> findOneByLogin(String login);

  @EntityGraph(attributePaths = "authorities")
  Optional<User> findOneWithAuthoritiesById(Long id);

  @EntityGraph(attributePaths = "authorities")
  @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
  Optional<User> findOneWithAuthoritiesByLogin(String login);

  @EntityGraph(attributePaths = "authorities")
  @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
  Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

  Page<User> findAllByLoginNot(Pageable pageable, String login);
}
