package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.ApplicationProperties;
import com.crm.ehelpdesk.domain.ProductCode;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.ComplaintDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class MailService {

  private final Logger log = LoggerFactory.getLogger(MailService.class);

  private static final String USER = "user";

  private static final String BASE_URL = "baseUrl";

  private ApplicationProperties applicationProperties;
  private final JavaMailSender javaMailSender;

  private final MessageSource messageSource;

  private final SpringTemplateEngine templateEngine;

  public MailService(ApplicationProperties applicationProperties, JavaMailSender javaMailSender,
                     MessageSource messageSource, SpringTemplateEngine templateEngine) {
    this.applicationProperties = applicationProperties;
    this.javaMailSender = javaMailSender;
    this.messageSource = messageSource;
    this.templateEngine = templateEngine;
  }

  @Async
  public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
    log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
      isMultipart, isHtml, to, subject, content);

    // Prepare message using a Spring helper
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
      message.setTo(to);
      message.setFrom(applicationProperties.getMail().getFrom());
      message.setSubject(subject);
      message.setText(content, isHtml);
      message.addInline("logo", new ClassPathResource("/static/images/logo.png"));
      javaMailSender.send(mimeMessage);
      log.debug("Sent email to User '{}'", to);
    } catch (MailException | MessagingException e) {
      log.warn("Email could not be sent to user '{}'", to, e);
    }
  }

  @Async
  public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
    Context context = new Context(Locale.ENGLISH);
    context.setVariable(USER, user);
    context.setVariable(BASE_URL, applicationProperties.getMail().getBaseUrl());
    String content = templateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
    sendEmail(user.getEmail(), subject, content, true, true);
  }

  @Async
  public void sendProductEmailFromTemplate(ProductCode productCode, String templateName, String titleKey) {
    Context context = new Context(Locale.ENGLISH);
    context.setVariable("productCode", productCode);
    context.setVariable(BASE_URL, applicationProperties.getMail().getBaseUrl());
    String content = templateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
    sendEmail(productCode.getUserEmail(), subject, content, true, true);
  }

  @Async
  public void setComplaintEmail(ComplaintDTO complaint, User user, String templateName, String titleKey) {
    Context context = new Context(Locale.ENGLISH);
    context.setVariable("user", user);
    context.setVariable("complaint", complaint);
    context.setVariable(BASE_URL, applicationProperties.getMail().getBaseUrl());
    String content = templateEngine.process(templateName, context);
    String subject = messageSource.getMessage(titleKey, null, Locale.ENGLISH);
    sendEmail(user.getEmail(), subject, content, true, true);
  }

  @Async
  public void sendActivationEmail(User user) {
    log.debug("Sending activation email to '{}'", user.getEmail());
    sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
  }

  @Async
  public void sendCreationEmail(User user) {
    log.debug("Sending creation email to '{}'", user.getEmail());
    sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
  }

  @Async
  public void sendPasswordResetMail(User user) {
    log.debug("Sending password reset email to '{}'", user.getEmail());
    sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
  }

  @Async
  public void sendUsernameEmail(User user) {
    log.debug("Sending username request email to '{}'", user.getEmail());
    sendEmailFromTemplate(user, "mail/usernameRequest", "email.username.title");
  }

  @Async
  public void sendOtpEmail(User user) {
    log.debug("Sending otp request email to '{}'", user.getEmail());
    sendEmailFromTemplate(user, "mail/otp", "email.otp.title");
  }

  @Async
  public void sendProductCode(ProductCode productCode) {
    log.debug("Sending product code email to '{}'", productCode.getUserEmail());
    sendProductEmailFromTemplate(productCode, "mail/productCodeEmail", "email.product.title");
  }

  @Async
  public void sendNewComplaintEmail(ComplaintDTO complaintDTO, User user) {
    log.debug("Sending new complaint email to '{}'", user.getEmail());
    setComplaintEmail(complaintDTO, user, "mail/newComplaintRaisedEmail", "email.complaint.new.title");
  }

  @Async
  public void sendComplaintAssignedEmail(ComplaintDTO complaintDTO, User user) {
    log.debug("Sending complaint assigned email to '{}'", user.getEmail());
    setComplaintEmail(complaintDTO, user, "mail/complaintAssignedEmail", "email.complaint.assigned.title");
  }

  @Async
  public void sendComplaintResolvedEmail(ComplaintDTO complaintDTO, User user) {
    log.debug("Sending complaint assigned email to '{}'", user.getEmail());
    setComplaintEmail(complaintDTO, user, "mail/complaintResolvedEmail", "email.complaint.resolved.title");
  }
}
