package com.crm.ehelpdesk.dto;

public class SearchDetailsDTO {

  private String accessState;
  private String tableToBeIncluded;
  private String argument;
  private String columnName;
  private String searchKeywordType;

  public SearchDetailsDTO(String accessState, String tableToBeIncluded, String argument,
                          String columnName, String searchKeywordType) {
    this.accessState = accessState;
    this.tableToBeIncluded = tableToBeIncluded;
    this.argument = argument;
    this.columnName = columnName;
    this.searchKeywordType = searchKeywordType;
  }

  public String getAccessState() {
    return accessState;
  }

  public String getTableToBeIncluded() {
    return tableToBeIncluded;
  }

  public String getArgument() {
    return argument;
  }

  public String getColumnName() {
    return columnName;
  }

  public String getSearchKeywordType() { return searchKeywordType; }

}
