package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import com.crm.ehelpdesk.dao.repository.ComplaintRepository;
import com.crm.ehelpdesk.domain.Authority;
import com.crm.ehelpdesk.domain.Complaint;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.ComplaintDTO;
import com.crm.ehelpdesk.security.SecurityUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final MailService mailService;

    public ComplaintService(ComplaintRepository complaintRepository, UserService userService, MailService mailService) {
        this.complaintRepository = complaintRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    public void createComplaint(ComplaintDTO complaintDTO) {
        complaintDTO.setRaisedBy(SecurityUtils.getCurrentUserLogin().get());
        Complaint complaint = new Complaint();
        complaint.setBrandId(complaintDTO.getBrand());
        complaint.setComplaintTypeId(complaintDTO.getComplaintType());
        complaint.setDescription(complaintDTO.getComplaintDescription());
        complaint.setProductId(complaintDTO.getProduct());
        complaint.setStatus("OPEN");
        complaint.setUserId(userService.getUserIdWithAuthoritiesByLogin());
        complaintRepository.save(complaint);
        List<User> managers = userService.getUserByAuthorities(Collections.singleton(new Authority("ROLE_MANAGER")));
        managers.forEach(manager -> {
            mailService.sendNewComplaintEmail(complaintDTO, manager);
        });
    }

    public List<ComplaintDTO> getComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        String authority = SecurityUtils.getUserAuthorities().get(0);
        if(authority.equals(AuthoritiesConstants.TECHNICIAN)) {
            Long technicianId = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId();
            complaints = complaints.stream().filter(complaint -> complaint.getTechnicianId() != null && complaint.getTechnicianId().equals(technicianId)).collect(Collectors.toList());
        }
        if(authority.equals(AuthoritiesConstants.CUSTOMER)) {
            Long getUserId = userService.getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId();
            complaints = complaints.stream().filter(complaint -> complaint.getUserId() != null && complaint.getUserId().equals(getUserId)).collect(Collectors.toList());
        }
        return complaints.stream().map(ComplaintDTO::new).collect(Collectors.toList());
    }

    public ComplaintDTO getComplaint(Long id) {
        return complaintRepository.findById(id).map(ComplaintDTO::new).get();
    }

    public void deleteComplaint(Long id) {
        complaintRepository.findById(id).ifPresent(complaintRepository::delete);
    }

    public List<Map<String, Object>> getTechnicians() {
        return userService.getUserByAuthorities(Collections.singleton(new Authority(AuthoritiesConstants.TECHNICIAN))).stream().map(technician -> {
            Map<String, Object> technicianDetails = new HashMap<>();
            technicianDetails.put("id", technician.getId());
            technicianDetails.put("name", technician.getFirstName() + technician.getLastName());
            technicianDetails.put("email", technician.getEmail());
            return technicianDetails;
        }).collect(Collectors.toList());
    }

    public void assignComplaint(Map<String, Object> complaintDetails) {
        Integer id = (Integer) complaintDetails.get("id");
        complaintRepository.findById(id.longValue()).ifPresent(complaint -> {
            String technicianId = (String) complaintDetails.get("technician");
            complaint.setTechnicianId(Long.parseLong(technicianId));
            complaint.setStatus("ASSIGNED");
            complaintRepository.save(complaint);
            User technician = userService.getUserById(Long.parseLong(technicianId));
            ComplaintDTO complaintDTO = new ComplaintDTO(complaint);
            User customer = userService.getUserById(complaint.getUserId());
            complaintDTO.setRaisedBy(customer.getLogin());
            mailService.sendComplaintAssignedEmail(complaintDTO, technician);
        });
    }

    public void resolveComplaint(Map<String, Object> complaintDetails) {
        Integer id = (Integer) complaintDetails.get("id");
        complaintRepository.findById(id.longValue()).ifPresent(complaint -> {
            String billAmount = (String) complaintDetails.get("billAmount");
            complaint.setBillAmount(Long.parseLong(billAmount));
            complaint.setStatus("RESOLVED");
            complaint.setBillNumber(Integer.parseInt(RandomStringUtils.randomNumeric(5)));
            complaintRepository.save(complaint);
            ComplaintDTO complaintDTO = new ComplaintDTO(complaint);
            User customer = userService.getUserById(complaint.getUserId());
            complaintDTO.setRaisedBy(customer.getLogin());
            mailService.sendComplaintResolvedEmail(complaintDTO, customer);

            List<User> managers = userService.getUserByAuthorities(Collections.singleton(new Authority("ROLE_MANAGER")));
            managers.forEach(manager -> {
                mailService.sendComplaintResolvedEmail(complaintDTO, manager);
            });
        });
    }
}
