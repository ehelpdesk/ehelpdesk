package com.crm.ehelpdesk.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = DBAccessDetails.TABLE_NAME)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DBAccessDetails implements Serializable {
  public static final String TABLE_NAME = "db_access_details";
  private Long dbAccessDetailsId;
  private Long entityId;
  private Long accessStateId;

  public DBAccessDetails() {
  }

  public DBAccessDetails(Long entityId, Long accessStateId) {
    this.entityId = entityId;
    this.accessStateId = accessStateId;
  }

  @Id
  @Column(name = "db_access_details_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getDbAccessDetailsId() {
    return dbAccessDetailsId;
  }

  public void setDbAccessDetailsId(Long dbAccessDetailsId) {
    this.dbAccessDetailsId = dbAccessDetailsId;
  }

  @Column(name = "entity_id")
  public Long getEntityId() {
    return entityId;
  }

  public void setEntityId(Long entityId) {
    this.entityId = entityId;
  }

  @Column(name = "access_states")
  public Long getAccessStateId() {
    return accessStateId;
  }

  public void setAccessStateId(Long accessStateId) {
    this.accessStateId = accessStateId;
  }
}
