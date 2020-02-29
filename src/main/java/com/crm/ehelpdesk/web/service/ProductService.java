package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.ProductCodeRepository;
import com.crm.ehelpdesk.domain.ProductCode;
import com.crm.ehelpdesk.dto.ProductCodeDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

@Service
public class ProductService {
    private final ProductCodeRepository productCodeRepository;

    public ProductService(ProductCodeRepository productCodeRepository) {
        this.productCodeRepository = productCodeRepository;
    }

    public Optional<ProductCodeDTO> validateProductCode(String code) {
        long productCode = 0;
        try {
            productCode = Long.parseLong(code);
        } catch (NumberFormatException nfe) {
            ProductCodeDTO productCodeDTO = new ProductCodeDTO();
            productCodeDTO.setCode(null);
            return Optional.of(productCodeDTO);
        }
        Optional<ProductCode> productCodeOptional = productCodeRepository.findByCodeAndUsed(productCode, false);
        return productCodeOptional.map(ProductCodeDTO::new);
    }
}
