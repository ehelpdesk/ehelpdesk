package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.domain.ComplaintType;

public class ComplaintTypeDTO {
    private Long id;
    private String type;

    public ComplaintTypeDTO(ComplaintType complaintType) {
        this.id = complaintType.getId();
        this.type = complaintType.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
