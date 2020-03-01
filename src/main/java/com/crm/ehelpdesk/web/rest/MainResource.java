package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.BrandDTO;
import com.crm.ehelpdesk.web.service.MainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class MainResource {
    private final MainService mainService;

    public MainResource(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping("getBrands")
    public List<BrandDTO> getBrands() {
        return mainService.getBrands();
    }


}
