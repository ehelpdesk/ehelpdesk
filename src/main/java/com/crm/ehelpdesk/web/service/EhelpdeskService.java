package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.constants.Constants;
import com.crm.ehelpdesk.config.util.EntityUtils;
import com.crm.ehelpdesk.dao.AbstractJDBCDaoImpl;
import com.crm.ehelpdesk.domain.MemberInfo;
import com.crm.ehelpdesk.dto.SearchResultDTO;
import com.crm.ehelpdesk.exception.LimitedAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class EhelpdeskService {

  private static final String WHERE = " where ";
  private String filterType = Constants.AND;
  private final AbstractJDBCDaoImpl abstractJDBCDaoImpl;
  private final MasterService masterService;
  private List<String> addressWildCardsKeys = Arrays.asList("state", "city", "pin", "area");

  public EhelpdeskService(AbstractJDBCDaoImpl abstractJDBCDaoImpl, MasterService masterService) {
    this.abstractJDBCDaoImpl = abstractJDBCDaoImpl;
    this.masterService = masterService;
  }

  @SuppressWarnings("unchecked")
  public List<MemberInfo> getSearchResultViaArgumentsForMember(Map<String, Object> filterArguments) throws InterruptedException {
    return abstractJDBCDaoImpl.getResultList(generateCustomQuery(filterArguments, MemberInfo.class, MemberInfo.TABLE_NAME), MemberInfo.class.getName());
  }

  @SuppressWarnings("unchecked")
  public List<SearchResultDTO> getSearchResultViaArguments(Map<String, Object> filterArguments) throws InterruptedException {
    Map<String, List<Object[]>> result;
    filterSpecialCharacters(filterArguments);
    filterType = "Partial".equals(filterArguments.get(Constants.FILTER_TYPE)) ? Constants.OR : Constants.AND;
    Map<String, Map<String, Map<String, String>>> searchDetails = masterService.getDBAccessDetailsDto()
      .get(filterArguments.get("state"));
    if (!CollectionUtils.isEmpty(searchDetails)) {
      buildAddressArgument(filterArguments);
      addKeyForSingleAddressIndetifier(filterArguments, searchDetails);
      filterArguments.put("limit", setLimit(searchDetails.size()));
      CountDownLatch latch = new CountDownLatch(searchDetails.size());
      result = searchViaMultiThreading(filterArguments, searchDetails, latch);
      wait(latch);
    } else {
      throw new LimitedAccessException(filterArguments.get("state"));
    }
    return ConvertResult(masterService.getColumnMap(searchDetails.keySet()), result);
  }

  private void addKeyForSingleAddressIndetifier(Map<String, Object> filterArguments,
                                                Map<String, Map<String, Map<String, String>>> searchDetails) {

    searchDetails.forEach((dbName, dbArguments) -> {
      if (isValidForSingleSearch(filterArguments, dbArguments, "name") ||
        isValidForSingleSearch(filterArguments, dbArguments,"mobileNo") ||
        isValidForSingleSearch(filterArguments, dbArguments,"identificationNumber")) {
        filterArguments.put("isSingleAddressSearch_"+dbName, false);
      } else {
        filterArguments.put("isSingleAddressSearch_"+dbName, true);
      }
    });
  }

  private boolean isValidForSingleSearch(Map<String, Object> filterArguments, Map<String, Map<String, String>> dbArguments,
                                         String value) {
    return !StringUtils.isEmpty(filterArguments.get(value)) && dbArguments.containsKey(value);
  }

  private Object setLimit(int size) {
    return (int) Math.ceil(100 / size);
  }

  private void filterSpecialCharacters(Map<String, Object> filterArguments) {
    filterArguments.forEach((key, value) -> filterArguments.put(key, value.toString().replaceAll("[^a-zA-Z0-9 ]", "")));
  }

  private List<SearchResultDTO> ConvertResult(Map<String, Map<String, Integer>> columnMap,
                                              Map<String, List<Object[]>> future) {
    List<SearchResultDTO> returnData = new ArrayList<>();
    future.forEach((key, value) -> {
      Map<String, Integer> columnMapDetails = columnMap.get(key);
      if (!CollectionUtils.isEmpty(value)) {
        returnData.addAll(getSearchResultDto(columnMapDetails, value));
      }
    });
    return returnData;
  }

  private List<SearchResultDTO> getSearchResultDto(Map<String, Integer> columnMapDetails, List<Object[]> value) {
    return value.stream().map(object -> getSearchResultDto(columnMapDetails, object)).collect(Collectors.toList());
  }

  private SearchResultDTO getSearchResultDto(Map<String, Integer> columnMapDetails, Object[] object) {
    return new SearchResultDTO(object[columnMapDetails.get("state")], object[columnMapDetails.get("operator")],
      object[columnMapDetails.get("mobile")], object[columnMapDetails.get("cname")], object[columnMapDetails.get("dob")],
      object[columnMapDetails.get("fname")], object[columnMapDetails.get("ladd")], object[columnMapDetails.get("padd")],
      object[columnMapDetails.get("alno")], object[columnMapDetails.get("email")], object[columnMapDetails.get("uid")],
      object[columnMapDetails.get("adr")], object[columnMapDetails.get("doa")]);
  }

  private void wait(CountDownLatch latch) throws InterruptedException {
    if (latch != null) {
      latch.await(10, TimeUnit.MINUTES);
    }
  }

  private void buildAddressArgument(Map<String, Object> filterArguments) {
    StringBuilder addressArgument = new StringBuilder();
    addressWildCardsKeys.forEach(s -> {
      String value = (String) filterArguments.get(s);
      addressArgument.append(!StringUtils.isEmpty(value) && !StringUtils.isEmpty(addressArgument.toString()) ? " " : "")
        .append(!StringUtils.isEmpty(value) ? value : "");
    });
    filterArguments.put("address", addressArgument.toString());
  }

  private Map<String, List<Object[]>> searchViaMultiThreading(Map<String, Object> filterArguments,
    Map<String, Map<String, Map<String, String>>> searchDetails, CountDownLatch latch) throws InterruptedException {
    ExecutorService executorService = Executors.newCachedThreadPool();
    Map<String, List<Object[]>> result = new HashMap<>();
    List<CallableLoopTask> list = new ArrayList<>();
    searchDetails.forEach((tableName, value) -> {
      list.add(new CallableLoopTask(latch, abstractJDBCDaoImpl, value, tableName,
        filterArguments, tableName));
    });
    executorService.invokeAll(list).forEach(mapFuture -> {
      try {
        result.putAll(mapFuture.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });
    executorService.shutdown();
    return result;
  }

  private String generateCustomQuery(Map<String, Object> filterArguments, Class entityClass, String tableName) {
    String finalQuery = "Select * from " + tableName;
    if (!CollectionUtils.isEmpty(filterArguments)) {
      finalQuery += WHERE;
      final StringBuilder customQuery = new StringBuilder("");
      final StringBuilder addressWildCardCustomQuery = new StringBuilder("");
      final StringBuilder nameWildCardCustomQuery = new StringBuilder("");
      filterType = "Partial".equals(filterArguments.get(Constants.FILTER_TYPE)) ? Constants.OR : Constants.AND;
      filterArguments.remove(Constants.FILTER_TYPE);
      filterArguments.forEach((key, value) -> {
        if (addressWildCardsKeys.contains(key) && !StringUtils.isEmpty(value)) {
          appendArgument(value, addressWildCardCustomQuery, key, entityClass);
        } else if ("name".equals(key) && !StringUtils.isEmpty(value)) {
          nameWildCardCustomQuery.append(getColumnName(key, entityClass)).append(" like '%").append(value).append("%'");
        } else {
          if (isStringBuilderEmpty(customQuery)) {
            appendString(entityClass, customQuery, key, value, "");
          } else {
            appendString(entityClass, customQuery, key, value, filterType);
          }
        }
      });
      finalQuery += customQuery.append(getClauseType(isClauseToBeAppended(customQuery, addressWildCardCustomQuery)))
        .append(addressWildCardCustomQuery)
        .append(getClauseType(isClauseToBeAppended(customQuery, nameWildCardCustomQuery)
          || isClauseToBeAppended(addressWildCardCustomQuery, nameWildCardCustomQuery)))
        .append(nameWildCardCustomQuery);
    }
    return finalQuery + " limit 100";
  }

  private String getClauseType(boolean isClauseToBeAppended) {
    return isClauseToBeAppended ? filterType : "";
  }

  private boolean isClauseToBeAppended(StringBuilder... queries) {
    return Arrays.stream(queries).noneMatch(this::isStringBuilderEmpty);
  }

  private boolean isStringBuilderEmpty(StringBuilder stringBuilder) {
    return StringUtils.isEmpty(stringBuilder.toString());
  }


  private void appendArgument(Object value, StringBuilder wildCardCustomQuery, String key, Class entityClass) {
    wildCardCustomQuery.append(!isStringBuilderEmpty(wildCardCustomQuery) ? filterType : "")
      .append(getColumnName(key, entityClass)).append(" like '%").append(value).append("%'");
  }

  private String getColumnName(String key, Class entityClass) {
    if (addressWildCardsKeys.contains(key)) {
      return EntityUtils.getNativeColumnName("residentialAddress", entityClass);
    } else {
      return EntityUtils.getNativeColumnName(key, entityClass);
    }
  }

  private void appendString(Class entityClass, StringBuilder customQuery, String key, Object value, String s) {
    if (!StringUtils.isEmpty(value)) {
      customQuery.append(s).append(getColumnName(key, entityClass)).append(" = '").append(value).append("'");
    }
  }

}
