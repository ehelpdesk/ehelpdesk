package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.AvailableStatesDTO;
import com.crm.ehelpdesk.web.service.MasterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MasterResource {

  private final MasterService masterService;

  public MasterResource(MasterService masterService) {
    this.masterService = masterService;
  }

  @GetMapping("/availableStates")
  public List<AvailableStatesDTO> getAvailableStates() {
    return masterService.getAvailableStates();
  }
}
