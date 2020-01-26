package com.crm.ehelpdesk.dao;

import com.crm.ehelpdesk.config.jdbc.AbstractJDBCDao;
import com.crm.ehelpdesk.domain.MemberInfo;
import com.crm.ehelpdesk.dto.AvailableStatesDTO;
import com.crm.ehelpdesk.dto.SearchDetailsDTO;
import com.crm.ehelpdesk.dto.TableColumnNameMappingDTO;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AbstractJDBCDaoImpl<T> extends AbstractJDBCDao {

  @PersistenceContext
  private EntityManager entityManager;

  public List<T> getResultList(String query, String className) {
    return this.getJdbcTemplate().query(query, (rs, rowNum) -> getObjectByName(className, rs));
  }

  public List getResultList(String query, int firstResult, int limit) {
    Query nativeQuery = entityManager.createNativeQuery(query);
    nativeQuery.setFirstResult(firstResult);
    nativeQuery.setMaxResults(limit);
    System.out.println("waiting for result");
    return nativeQuery.getResultList();
  }

  @SuppressWarnings("unchecked")
  private T getObjectByName(String className, ResultSet rs) throws SQLException {
    if (MemberInfo.class.getName().equals(className)) {
      return (T) getMemberInfoClassDetails(rs);
    } else if (SearchDetailsDTO.class.getName().equals(className)) {
      return (T) getSearchDetailsDtoClassDetails(rs);
    } else if (TableColumnNameMappingDTO.class.getName().equals(className)) {
      return (T) getTableColumnNameMappingClassDetails(rs);
    } else if (AvailableStatesDTO.class.getName().equals(className)) {
      return (T) getAvailableStates(rs);
    }
    return null;
  }

  private AvailableStatesDTO getAvailableStates(ResultSet rs) throws SQLException {
    return new AvailableStatesDTO(rs.getLong(1), rs.getString(2));
  }

  private TableColumnNameMappingDTO getTableColumnNameMappingClassDetails(ResultSet rs) throws SQLException {
    return new TableColumnNameMappingDTO(rs.getLong(1), rs.getString(2),
      rs.getString(3), rs.getString(4), rs.getInt(5));
  }

  private Map<String, String> getRawData(ResultSet rs, List<Map<String, String>> returnData) throws SQLException {
    Map<String, String> rows = new HashMap<>();
    ResultSetMetaData md = rs.getMetaData();
    int columns = md.getColumnCount();
    while (rs.next()) {
      rows = new HashMap<>();
      for(int i = 1; i <= columns; i++) {
        rows.put(md.getColumnName(i), rs.getString(i));
      }
      returnData.add(rows);
    }
    return rows;
  }

  private SearchDetailsDTO getSearchDetailsDtoClassDetails(ResultSet rs) throws SQLException {
    return new SearchDetailsDTO(rs.getString(1), rs.getString(2), rs.getString(3),
      rs.getString(4), rs.getString(5));
  }

  private MemberInfo getMemberInfoClassDetails(ResultSet rs) throws SQLException {
    MemberInfo memberInfo = new MemberInfo();
    memberInfo.setMemberInfoId(rs.getLong(1));
    memberInfo.setName(rs.getString(2));
    memberInfo.setEmailId(rs.getString(3));
    memberInfo.setMobileNo(rs.getString(4));
    memberInfo.setResidentialAddress(rs.getString(5));
    memberInfo.setLocation(rs.getString(6));
    return memberInfo;
  }
}
