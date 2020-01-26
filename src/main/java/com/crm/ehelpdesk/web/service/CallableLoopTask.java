package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.constants.Constants;
import com.crm.ehelpdesk.dao.AbstractJDBCDaoImpl;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class CallableLoopTask implements Callable<Map<String, List<Object[]>>> {

  private final CountDownLatch countDownLatch;
  private final AbstractJDBCDaoImpl abstractJDBCDaoImpl;
  private final Map<String, Map<String, String>> searchDetails;
  private final String tableName;
  private final Map<String, Object> filterArguments;
  private final String key;

  CallableLoopTask(CountDownLatch countDownLatch, AbstractJDBCDaoImpl abstractJDBCDaoImpl,
                   Map<String, Map<String, String>> searchDetails, String tableName,
                   Map<String, Object> filterArguments, String key) {
    this.countDownLatch = countDownLatch;
    this.abstractJDBCDaoImpl = abstractJDBCDaoImpl;
    this.searchDetails = searchDetails;
    this.tableName = tableName;
    this.filterArguments = filterArguments;
    this.key = key;
  }

  @Override
  public Map<String, List<Object[]>> call() {
    Map<String, List<Object[]>> data = new HashMap<>();
    try {
      System.out.println("called");
      data.put(key, abstractJDBCDaoImpl.getResultList(generateSearchQuery(searchDetails, tableName),
        Integer.parseInt(filterArguments.get("firstResult").toString()), 100));
    } catch (Exception e) {
      throw e;
    } finally {
      if (countDownLatch != null) {
        countDownLatch.countDown();
      }
    }
    return data;
  }

  private String generateSearchQuery(Map<String, Map<String, String>> searchDetails, String tableName) {
    Map<String, String> keysQueried = new HashMap<>();
    StringBuilder finalQuery = new StringBuilder();
    if (!CollectionUtils.isEmpty(searchDetails)) {
      finalQuery.append("select * from ").append(tableName).append(" where ");
      String filterType = "Partial".equals(filterArguments.get(Constants.FILTER_TYPE)) ? Constants.OR : Constants.AND;
      boolean isSingleAddressSearch = (boolean)filterArguments.get("isSingleAddressSearch_"+tableName);
      searchDetails.forEach((argumentKey, value) -> {
        if (!CollectionUtils.isEmpty(value) && filterArguments.containsKey(argumentKey)) {
          value.forEach((columnName, s2) -> {
            if ("fts".equals(s2)) {
              appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "fullTextSearch", true, true);
            } else if ("wildcard".equals(s2)) {
              appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "wildCard", true, true);
            } else if ("ftsOrwildcard".equals(s2)) {
              finalQuery.append("(");
              appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "fullTextSearch", false, true);
              finalQuery.append(Constants.OR).append("(");
              appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "wildCard", true, false);
              finalQuery.append("))");
            } else if ("equals".equals(s2)) {
              appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "equals", true, true);
            } else if ("ftsOrWildcardViaArgument".equals(s2)) {
              if (isSingleAddressSearch) {
                appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "fullTextSearch", true, true);
              } else {
                appendQuery(keysQueried, finalQuery, filterType, argumentKey, value, "wildCard", true, true);
              }
            }
          });
        }
      });
    }
    return finalQuery.toString();
  }

  private void appendQuery(Map<String, String> keysQueried, StringBuilder finalQuery, String filterType, String argumentKey,
                           Map<String, String> value, String searchType, boolean needToBeAddedInKeysQueried, boolean needToAppendClause) {
    finalQuery.append(getClause(finalQuery, !keysQueried.containsKey(argumentKey)
      && !StringUtils.isEmpty(filterArguments.get(argumentKey)) && needToAppendClause, filterType)).append(
      getStringQuery(argumentKey, value, keysQueried, searchType, filterType, needToBeAddedInKeysQueried));
  }

  private String getStringQuery(String argumentKey, Map<String, String> value, Map<String, String> keysQueried,
                                String queryType, String filterType, boolean needToBeAddedInKeysQueried) {
    String argumentValue = (String) filterArguments.get(argumentKey);
    if (!StringUtils.isEmpty(argumentValue) && !keysQueried.containsKey(argumentKey)) {
      StringBuilder columnNames = new StringBuilder();
      AtomicInteger currentIndex = new AtomicInteger(0);
      value.forEach((s, s2) -> {
        currentIndex.getAndIncrement();
        if (currentIndex.get() < value.size())
          columnNames.append(s).append(", ");
        else
          columnNames.append(s);
      });
      StringBuilder query = new StringBuilder();
      if ("wildCard".equals(queryType)) {
        Arrays.stream(getArgumentsAsArray(argumentValue)).forEach(s -> {
          query.append(!StringUtils.isEmpty(query.toString()) ? " and " : "")
            .append(columnNames.toString())
            .append(" like '%")
            .append(s).append("%'");
        });
      } else if ("fullTextSearch".equals(queryType)) {
        query.append(" match(").append(columnNames.toString()).append(") against ('")
          .append(getArguments(argumentValue, filterType)).append("' in boolean mode)");
      } else if ("equals".equals(queryType)) {
        query.append(" ").append(columnNames.toString()).append(" = ").append("'").append(argumentValue).append("'");
      }
      if (needToBeAddedInKeysQueried) {
        keysQueried.put(argumentKey, query.toString());
      }
      return query.toString();
    }
    return "";
  }

  private String getClause(StringBuilder finalQuery, boolean isNeedsToBeAppended, String filterType) {
    return (finalQuery.toString().contains("match") ||
      finalQuery.toString().contains("like")) && isNeedsToBeAppended ? filterType : "";
  }

  private String getArguments(Object argument, String filterType) {
    StringBuilder finalArgument = new StringBuilder();
    String[] data = argument.toString().split(" ");
    Arrays.stream(data).forEach(s -> {
      finalArgument.append(!StringUtils.isEmpty(finalArgument.toString()) ? " " : "")
        .append(Constants.AND.equals(filterType) ? "+" : "").append(s);
    });
    return finalArgument.toString();
  }

  private String[] getArgumentsAsArray(Object argument) {
    return argument.toString().split(" ");
  }
}
