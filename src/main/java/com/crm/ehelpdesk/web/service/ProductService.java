package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.ProductCodeRepository;
import com.crm.ehelpdesk.domain.ProductCode;
import com.crm.ehelpdesk.dto.ProductCodeDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    private final ProductCodeRepository productCodeRepository;
    private final MailService mailService;

    public ProductService(ProductCodeRepository productCodeRepository, MailService mailService) {
        this.productCodeRepository = productCodeRepository;
        this.mailService = mailService;
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

    public void saveProductPurchase(ProductCodeDTO productCodeDTO) {
        ProductCode productCode = new ProductCode();
        productCode.setUserFirstName(productCodeDTO.getUserFirstName());
        productCode.setUserLastName(productCodeDTO.getUserLastName());
        productCode.setUserEmail(productCodeDTO.getUserEmail());
        productCode.setUserMobileNo(productCodeDTO.getUserMobileNo());
        productCode.setUserAddress(productCodeDTO.getUserAddress());
        productCode.setUserCity(productCodeDTO.getUserCity());
        productCode.setProductId(productCodeDTO.getProductDTO().getId().intValue());
        ProductCode savedProductCode = productCodeRepository.save(productCode);
        mailService.sendProductCode(savedProductCode);
    }
}
