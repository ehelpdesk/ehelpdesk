package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.ComplaintDTO;
import com.crm.ehelpdesk.web.service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ComplaintResource {
    private final ComplaintService complaintService;

    public ComplaintResource(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/complaints")
    public void createComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) {
        complaintService.createComplaint(complaintDTO);
    }
}
