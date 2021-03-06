package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.ProductCodeDTO;
import com.crm.ehelpdesk.dto.ProductDTO;
import com.crm.ehelpdesk.web.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("getProducts")
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/getTopProducts")
    public List<ProductDTO> getTopProducts() {
        return productService.getTopProducts();
    }
}
