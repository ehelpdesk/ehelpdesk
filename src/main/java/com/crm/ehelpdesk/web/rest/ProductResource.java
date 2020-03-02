package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.ProductCodeDTO;
import com.crm.ehelpdesk.web.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class ProductResource {
    private final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("addProductPurchase")
    public void addProductPurchase(@RequestBody ProductCodeDTO productCodeDTO) {
        productService.saveProductPurchase(productCodeDTO);
    }
}
