package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.ComplaintType;
import com.crm.ehelpdesk.domain.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryDTO {

    public ProductCategoryDTO() {
    }

    public ProductCategoryDTO(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    private Long id;

    private String categoryName;

    private List<ComplaintTypeDTO> complaintTypes;

    public ProductCategoryDTO(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.categoryName = productCategory.getCategoryName();
        this.complaintTypes = productCategory.getComplaintTypes().stream().map(ComplaintTypeDTO::new).collect(Collectors.toList());
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

    public List<ComplaintTypeDTO> getComplaintTypes() {
        return complaintTypes;
    }

    public void setComplaintTypes(List<ComplaintTypeDTO> complaintTypes) {
        this.complaintTypes = complaintTypes;
    }
}
