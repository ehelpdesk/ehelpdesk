package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.dao.repository.ComplaintRepository;
import com.crm.ehelpdesk.domain.Complaint;
import com.crm.ehelpdesk.dto.ComplaintDTO;
import org.springframework.stereotype.Service;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    public void createComplaint(ComplaintDTO complaintDTO) {
        Complaint complaint = new Complaint();
        complaint.setBrandId(complaintDTO.getBrand());
        complaint.setComplaintTypeId(complaintDTO.getComplaintType());
        complaint.setDescription(complaintDTO.getComplaintDescription());
        complaint.setProductId(complaintDTO.getProduct());
        complaint.setStatus("OPEN");
        complaintRepository.save(complaint);
    }
}
