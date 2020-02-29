package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.Brand;

import javax.persistence.*;

public class BrandDTO {
    private Long id;

    private String name;

    private String country;

    public BrandDTO() {
    }

    public BrandDTO(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public BrandDTO(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.country = brand.getCountry();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
