package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.ProductCode;

public class ProductCodeDTO {
    private Long code;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userMobileNo;
    private String userAddress;
    private String userCity;
    private ProductDTO productDTO;

    public ProductCodeDTO(ProductCode productCode) {
        this.code = productCode.getCode();
        this.userFirstName = productCode.getUserFirstName();
        this.userLastName = productCode.getUserLastName();
        this.userEmail = productCode.getUserEmail();
        this.userMobileNo = productCode.getUserMobileNo();
        this.userAddress = productCode.getUserAddress();
        this.productDTO = new ProductDTO(productCode.getProduct());
    }

    public ProductCodeDTO(Long code, String userFirstName, String userLastName, String userEmail, String userMobileNo, String userAddress, ProductDTO productDTO) {
        this.code = code;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userMobileNo = userMobileNo;
        this.userAddress = userAddress;
        this.productDTO = productDTO;
    }

    public ProductCodeDTO() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }
}
