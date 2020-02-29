package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.ProductCategory;

import javax.persistence.*;

public class ProductCategoryDTO {

    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    private Long id;

    private String categoryName;

    public ProductCategoryDTO(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.categoryName = productCategory.getCategoryName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
