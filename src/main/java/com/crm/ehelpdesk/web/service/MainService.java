package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.BrandRepository;
import org.springframework.stereotype.Service;
import com.crm.ehelpdesk.dto.BrandDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainService {
    private final BrandRepository brandRepository;

    public MainService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> getBrands() {
        return brandRepository.findAll().stream().map(BrandDTO::new).collect(Collectors.toList());
    }
}
