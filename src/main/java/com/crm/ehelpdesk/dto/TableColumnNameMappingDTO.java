package com.crm.ehelpdesk.dto;

public class TableColumnNameMappingDTO {

  private Long id;
  private String tableName;
  private String columnName;
  private String displayName;
  private int columnIndex;

  public TableColumnNameMappingDTO(Long id, String tableName,
                                   String columnName, String displayName, int columnIndex) {
    this.id = id;
    this.tableName = tableName;
    this.columnName = columnName;
    this.displayName = displayName;
    this.columnIndex = columnIndex;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getColumnName() {
    return columnName;
  }

  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }
}
