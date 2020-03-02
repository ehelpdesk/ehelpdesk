package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.Complaint;

public class ComplaintDTO {
    private Long id;
    private String raisedBy;
    private Long brand;
    private Long product;
    private String productName;
    private String productCategory;
    private Long complaintType;
    private String complaintTypeName;
    private String complaintDescription;
    private String status;

    public ComplaintDTO() {
    }

    public ComplaintDTO(Complaint complaint) {
        this.id = complaint.getId();
        this.productName = complaint.getProduct().getProductName();
        this.complaintTypeName = complaint.getComplaintType().getType();
        this.complaintDescription = complaint.getDescription();
        this.status = complaint.getStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisedBy() {
        return raisedBy;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public Long getBrand() {
        return brand;
    }

    public void setBrand(Long brand) {
        this.brand = brand;
    }

    public Long getProduct() {
        return product;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Long getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(Long complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintDescription() {
        return complaintDescription;
    }

    public void setComplaintDescription(String complaintDescription) {
        this.complaintDescription = complaintDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComplaintTypeName() {
        return complaintTypeName;
    }

    public void setComplaintTypeName(String complaintTypeName) {
        this.complaintTypeName = complaintTypeName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
