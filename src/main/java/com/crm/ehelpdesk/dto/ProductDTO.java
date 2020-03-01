package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.Product;

public class ProductDTO {
    private Long id;

    private String productName;

    private ProductCategoryDTO productCategory;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public ProductDTO(Long id, String productName, ProductCategoryDTO productCategory) {
        this.id = id;
        this.productName = productName;
        this.productCategory = productCategory;
    }

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.productCategory = new ProductCategoryDTO(product.getProductCategory());
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

    public ProductCategoryDTO getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryDTO productCategory) {
        this.productCategory = productCategory;
    }
}
