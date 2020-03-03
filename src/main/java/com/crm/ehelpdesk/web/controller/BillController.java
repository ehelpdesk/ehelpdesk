package com.crm.ehelpdesk.web.controller;

import com.crm.ehelpdesk.dao.repository.ComplaintRepository;
import com.crm.ehelpdesk.dao.repository.CustomerRepository;
import com.crm.ehelpdesk.domain.Customer;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.web.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class BillController {
    private final ComplaintRepository complaintRepository;
    private final CustomerRepository customerRepository;
    private final UserService userService;

    public BillController(ComplaintRepository complaintRepository, CustomerRepository customerRepository, UserService userService) {
        this.complaintRepository = complaintRepository;
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    @GetMapping("/bill/{complaintId}")
    public String bill(@PathVariable Long complaintId, Model model) {
        complaintRepository.findById(complaintId).ifPresent(complaint -> {
            model.addAttribute("complaint", complaint);
            User user = userService.getUserById(complaint.getUserId());
            Customer customer = customerRepository.findByUsername(user.getLogin());
            model.addAttribute("clientName", customer.getUsername());
            model.addAttribute("clientAddress", customer.getAddress());
        });
        String date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
        model.addAttribute("date", date);
        return "complaintBill";
    }
}
