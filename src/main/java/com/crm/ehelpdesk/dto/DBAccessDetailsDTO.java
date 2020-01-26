package com.crm.ehelpdesk.dto;

public class DBAccessDetailsDTO {

  private Long dbAccessDetailsId;
  private Long entityId;
  private Long accessStateId;

  public DBAccessDetailsDTO(Long entityId, Long accessStateId) {
    this.entityId = entityId;
    this.accessStateId = accessStateId;
  }

  public Long getDbAccessDetailsId() {
    return dbAccessDetailsId;
  }

  public void setDbAccessDetailsId(Long dbAccessDetailsId) {
    this.dbAccessDetailsId = dbAccessDetailsId;
  }

  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  public Long getAccessStateId() {
    return accessStateId;
  }

  public void setAccessStateId(Long accessStateId) {
    this.accessStateId = accessStateId;
  }
}
