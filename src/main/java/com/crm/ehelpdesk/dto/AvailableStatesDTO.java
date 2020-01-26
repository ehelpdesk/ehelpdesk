package com.crm.ehelpdesk.dto;

public class AvailableStatesDTO {

  private Long availableStateId;
  private String name;

  public AvailableStatesDTO() {
  }

  public AvailableStatesDTO(Long availableStateId, String name) {
    this.availableStateId = availableStateId;
    this.name = name;
  }

  public Long getAvailableStateId() {
    return availableStateId;
  }

  public void setAvailableStateId(Long availableStateId) {
    this.availableStateId = availableStateId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
