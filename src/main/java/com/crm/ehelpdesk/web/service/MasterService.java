package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import com.crm.ehelpdesk.dao.AbstractJDBCDaoImpl;
import com.crm.ehelpdesk.dao.repository.DBAccessDetailsRepository;
import com.crm.ehelpdesk.domain.DBAccessDetails;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.AvailableStatesDTO;
import com.crm.ehelpdesk.dto.SearchDetailsDTO;
import com.crm.ehelpdesk.dto.TableColumnNameMappingDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class MasterService {

  private static final String IS_SUPER_ADMIN = "isSuperAdmin";
  private static final String ENTITY_ID = "entityId";
  private final AbstractJDBCDaoImpl abstractJDBCDaoImpl;
  private final UserService userService;
  private final DBAccessDetailsRepository dbAccessDetailsRepository;

  public MasterService(AbstractJDBCDaoImpl abstractJDBCDaoImpl, UserService userService, DBAccessDetailsRepository dbAccessDetailsRepository) {
    this.abstractJDBCDaoImpl = abstractJDBCDaoImpl;
    this.userService = userService;
    this.dbAccessDetailsRepository = dbAccessDetailsRepository;
  }

  Map<String, Map<String, Map<String, Map<String, String>>>> getDBAccessDetailsDto() {
    Optional<User> user = this.userService.getUserWithAuthorities();
    Map<String, Object> entityIdMap = user.map(this::getUserId).orElse(new HashMap<>());
    return ((boolean) entityIdMap.get(IS_SUPER_ADMIN) ? getSearchDetailsForSuperAdmin() :
      getSearchDetails(entityIdMap.get(ENTITY_ID)));
  }

  public void updateDBAccessDetails(Set<AvailableStatesDTO> availableStatesDtos, Long id) {
    dbAccessDetailsRepository.deleteByEntityId(id);
    createDBAccessDetails(availableStatesDtos, id);
  }

  public void createDBAccessDetails(Set<AvailableStatesDTO> availableStatesDtos, Long id) {
    List<DBAccessDetails> dbAccessDetails = new ArrayList<>();
    if (!CollectionUtils.isEmpty(availableStatesDtos)) {
      availableStatesDtos.forEach(availableStatesDto -> dbAccessDetails
        .add(new DBAccessDetails(id, availableStatesDto.getAvailableStateId())));
    }
    saveAllDBAccessDetails(dbAccessDetails);
  }

  private void saveAllDBAccessDetails(List<DBAccessDetails> dbAccessDetails) {
    if (!CollectionUtils.isEmpty(dbAccessDetails)) {
      dbAccessDetailsRepository.saveAll(dbAccessDetails);
    }
  }

  private Map<String, Map<String, Map<String, Map<String, String>>>> getSearchDetailsForSuperAdmin() {
    return convertDBAccessDetailsDtoToMap((List<SearchDetailsDTO>) abstractJDBCDaoImpl.getResultList(
      "SELECT ass.name, tsm.table_to_be_included, sd.arguments, sd.column_name, sd.search_keyword_type " +
        "FROM search_details sd, available_states ass, table_state_mapping tsm " +
        "where ass.available_states_id = tsm.available_state_id and sd.db_name = tsm.table_to_be_included", SearchDetailsDTO.class.getName()));
  }

  private Map<String, Map<String, Map<String, Map<String, String>>>> getSearchDetails(Object entityId) {
    return convertDBAccessDetailsDtoToMap((List<SearchDetailsDTO>) abstractJDBCDaoImpl.getResultList(
      "SELECT ass.name, tsm.table_to_be_included, db.arguments, db.column_name, db.search_keyword_type " +
        "FROM ehelpdesk.search_details db, db_access_details sd, available_states ass, table_state_mapping tsm " +
        "where sd.entity_id = " + entityId + " and " +
        "tsm.table_to_be_included = db.db_name and ass.available_states_id = sd.access_states " +
        "and tsm.available_state_id = sd.access_states", SearchDetailsDTO.class.getName()));
  }

  private Map<String, Object> getUserId(User user) {
    Map<String, Object> returnData = new HashMap<String, Object>() {{
      put(IS_SUPER_ADMIN, false);
    }};
    String authorityName = user.getAuthorities().getName();
    if (AuthoritiesConstants.USER.equals(authorityName))
      returnData.put(ENTITY_ID, user.getOwnerDetails().getId());
    if (AuthoritiesConstants.ADMIN.equals(authorityName))
      returnData.put(ENTITY_ID, user.getId());
    if (AuthoritiesConstants.SUPER_ADMIN.equals(authorityName))
      returnData.put(IS_SUPER_ADMIN, true);
    return returnData;
  }

  public List<AvailableStatesDTO> getAvailableStates() {
    return abstractJDBCDaoImpl.getResultList("Select * from available_states", AvailableStatesDTO.class.getName());
  }

  public List<AvailableStatesDTO> getAvailableStatesByUserId(Long id) {
    return abstractJDBCDaoImpl.getResultList("Select * from available_states where available_states_id IN " +
      "(select access_states from db_access_details where entity_id = "+id+")", AvailableStatesDTO.class.getName());
  }

  private Map<String, Map<String, Map<String, Map<String, String>>>> convertDBAccessDetailsDtoToMap(List<SearchDetailsDTO> resultList) {
    return resultList.stream()
      .collect(Collectors.groupingBy(SearchDetailsDTO::getAccessState,
        Collectors.groupingBy(SearchDetailsDTO::getTableToBeIncluded,
          Collectors.groupingBy(SearchDetailsDTO::getArgument,
            Collectors.toMap(SearchDetailsDTO::getColumnName, SearchDetailsDTO::getSearchKeywordType)))));
  }

  Map<String, Map<String, Integer>> getColumnMap(Set<String> keySet) {
    StringBuilder ColumnName = new StringBuilder();
    AtomicInteger index = new AtomicInteger(1);
    if (!CollectionUtils.isEmpty(keySet)) {
      keySet.forEach(s -> {
        ColumnName.append("'").append(s).append("'").append(index.get() < keySet.size() ? "," : "");
        index.getAndIncrement();
      });
    }
    return convertColumnMap(abstractJDBCDaoImpl.getResultList("Select * from table_columnname_mapping where " +
      "table_name in (" + ColumnName.toString() + ")", TableColumnNameMappingDTO.class.getName()));
  }

  private Map<String, Map<String, Integer>> convertColumnMap(List<TableColumnNameMappingDTO> resultList) {
    Map<String, Map<String, Integer>> columnMap = new HashMap<>();
    if (!CollectionUtils.isEmpty(resultList)) {
      columnMap = resultList.stream().collect(Collectors.groupingBy(TableColumnNameMappingDTO::getTableName,
        Collectors.toMap(TableColumnNameMappingDTO::getColumnName, TableColumnNameMappingDTO::getColumnIndex)));
    }
    return columnMap;
  }
}
