package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.ComplaintRepository;
import com.crm.ehelpdesk.dao.repository.ProductCodeRepository;
import com.crm.ehelpdesk.dao.repository.ProductRepository;
import com.crm.ehelpdesk.domain.Complaint;
import com.crm.ehelpdesk.domain.ProductCode;
import com.crm.ehelpdesk.dto.ProductCodeDTO;
import com.crm.ehelpdesk.dto.ProductDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductCodeRepository productCodeRepository;
    private final MailService mailService;
    private final ProductRepository productRepository;
    private final ComplaintRepository complaintRepository;

    public ProductService(ProductCodeRepository productCodeRepository, MailService mailService, ProductRepository productRepository, ComplaintRepository complaintRepository) {
        this.productCodeRepository = productCodeRepository;
        this.mailService = mailService;
        this.productRepository = productRepository;
        this.complaintRepository = complaintRepository;
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

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public List<ProductDTO> getTopProducts() {
        List<Complaint> complaints = complaintRepository.findAll();
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductDTO productDTO = new ProductDTO(product);
                    long count = complaints.stream().filter(complaint -> complaint.getProductId().equals(productDTO.getId())).count();
                    productDTO.setNumberOfComplaints((int) count);
                    return productDTO;
                })
                .sorted(Comparator.comparing(ProductDTO::getNumberOfComplaints))
                .collect(Collectors.toList());
    }
}
