package com.crm.ehelpdesk.config.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public abstract class AbstractJDBCDao {

  private final Logger log = LoggerFactory.getLogger(AbstractJDBCDao.class);

  private DataSource dataSource;

  protected JdbcTemplate jdbc;

  private NamedParameterJdbcTemplate jdbcTemplate;

  private SimpleJdbcCall simpleJdbcCall;

  private SimpleJdbcInsert simpleJdbcInsert;

  public AbstractJDBCDao() {}

  @PostConstruct
  protected final void init() {
    log.debug("Initializing JDBC datasource");
    this.jdbc = new JdbcTemplate(dataSource);
    this.jdbcTemplate = new NamedParameterJdbcTemplate(this.jdbc);
    this.simpleJdbcCall = new SimpleJdbcCall(this.jdbc);
    this.simpleJdbcInsert = new SimpleJdbcInsert(this.jdbc);
  }

  @Autowired(required = false)
  public final void setDataSource(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  protected final JdbcTemplate getJdbcTemplate() {
    return this.jdbc;
  }

  protected final NamedParameterJdbcTemplate getNamedJdbcTemplate() {
    return jdbcTemplate;
  }

  protected final SimpleJdbcCall getSimpleJdbcCall() {
    return simpleJdbcCall;
  }

  protected final SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleJdbcInsert;
  }

  protected Map<String, Object> queryForMapWithExceptionCheck(
      String sql, @Nullable Object... args) {
    Map<String, Object> result;
    try {
      result = getJdbcTemplate().queryForMap(sql, args);
    } catch (EmptyResultDataAccessException ex) {
      log.warn(
          "Error when query for map performed: sql = {}, params = {} and exception = {}",
          sql,
          args,
          ex);
      result = Collections.emptyMap();
    }
    return result;
  }

  protected List<Map<String, Object>> queryForListWithExceptionCheck(
      String sql, @Nullable Object... args) {
    List<Map<String, Object>> result;
    try {
      result = getJdbcTemplate().queryForList(sql, args);
    } catch (UncategorizedSQLException ex) {
      log.warn(
          "Error when query for list performed: sql = {}, params = {} and exception = {}",
          sql,
          args,
          ex);
      result = Collections.emptyList();
    }
    return result;
  }
}
