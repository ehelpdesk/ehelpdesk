package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.SearchResultDTO;
import com.crm.ehelpdesk.web.service.EhelpdeskService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/main")
public class EhelpdeskResource {

  private final EhelpdeskService queryfierService;
  private final PasswordEncoder passwordEncoder;

  public EhelpdeskResource(EhelpdeskService queryfierService, PasswordEncoder passwordEncoder) {
    this.queryfierService = queryfierService;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping("/search")
  public List<SearchResultDTO> Search(@RequestBody Map<String, Object> filterArguments) throws InterruptedException {
    return queryfierService.getSearchResultViaArguments(filterArguments);
  }

  @GetMapping("/password")
  public String password() {
    return passwordEncoder.encode("admin");
  }
}
