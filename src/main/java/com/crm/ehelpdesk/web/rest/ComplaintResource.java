package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.dto.ComplaintDTO;
import com.crm.ehelpdesk.web.service.ComplaintService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ComplaintResource {
    private final ComplaintService complaintService;

    public ComplaintResource(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping("/complaints")
    public List<ComplaintDTO> getComplaints() {
        return complaintService.getComplaints();
    }

    @PostMapping("/complaints")
    public void createComplaint(@Valid @RequestBody ComplaintDTO complaintDTO) {
        complaintService.createComplaint(complaintDTO);
    }

    @GetMapping("/complaints/{id}")
    public ComplaintDTO getComplaint(@PathVariable Long id) {
        return complaintService.getComplaint(id);
    }

    @DeleteMapping("/complaints/{id}")
    public void deleteComplaint(@PathVariable Long id) {
        complaintService.deleteComplaint(id);
    }

    @GetMapping("/getTechnicians")
    public List<Map<String, Object>> getTechnicians() {
        return complaintService.getTechnicians();
    }

    @PostMapping("/assignComplaint")
    public void assignComplaint(@Valid @RequestBody Map<String, Object> complaintDetails) {
        complaintService.assignComplaint(complaintDetails);
    }

    @PostMapping("/resolveComplaint")
    public void resolveComplaint(@Valid @RequestBody Map<String, Object> complaintDetails) {
        complaintService.resolveComplaint(complaintDetails);
    }
}
