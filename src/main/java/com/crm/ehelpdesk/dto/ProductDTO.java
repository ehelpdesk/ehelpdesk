package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.Product;

import javax.persistence.Column;

public class ProductDTO {
    private Long id;

    private String productName;

    private BrandDTO brandDTO;
    private ProductCategoryDTO productCategoryDTO;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public ProductDTO(Long id, String productName, BrandDTO brandDTO, ProductCategoryDTO productCategoryDTO) {
        this.id = id;
        this.productName = productName;
        this.brandDTO = brandDTO;
        this.productCategoryDTO = productCategoryDTO;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.brandDTO = new BrandDTO(product.getBrand());
        this.productCategoryDTO = new ProductCategoryDTO(product.getProductCategory());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BrandDTO getBrandDTO() {
        return brandDTO;
    }

    public void setBrandDTO(BrandDTO brandDTO) {
        this.brandDTO = brandDTO;
    }

    public ProductCategoryDTO getProductCategoryDTO() {
        return productCategoryDTO;
    }

    public void setProductCategoryDTO(ProductCategoryDTO productCategoryDTO) {
        this.productCategoryDTO = productCategoryDTO;
    }
}
