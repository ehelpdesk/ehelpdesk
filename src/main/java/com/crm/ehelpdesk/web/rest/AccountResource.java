package com.crm.ehelpdesk.web.rest;


import com.crm.ehelpdesk.dao.repository.UserRepository;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.*;
import com.crm.ehelpdesk.exception.EmailAlreadyUsedException;
import com.crm.ehelpdesk.exception.EmailNotFoundException;
import com.crm.ehelpdesk.exception.InvalidPasswordException;
import com.crm.ehelpdesk.security.SecurityUtils;
import com.crm.ehelpdesk.web.service.MailService;
import com.crm.ehelpdesk.web.service.ProductService;
import com.crm.ehelpdesk.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

  private static class AccountResourceException extends RuntimeException {
    private AccountResourceException(String message) {
      super(message);
    }
  }

  private final Logger log = LoggerFactory.getLogger(AccountResource.class);

  private final UserRepository userRepository;

  private final UserService userService;

  private final MailService mailService;

  private final ProductService productService;

  public AccountResource(UserRepository userRepository, UserService userService, MailService mailService, ProductService productService) {

    this.userRepository = userRepository;
    this.userService = userService;
    this.mailService = mailService;
    this.productService = productService;
  }

  @GetMapping("/isLoginActive")
  public boolean isLoginActive() {
    return SecurityUtils.isAuthenticated();
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
    if (!checkPasswordLength(managedUserVM.getPassword())) {
      throw new InvalidPasswordException();
    }
    User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
    mailService.sendActivationEmail(user);
  }

  @PostMapping("/validateProductCode")
  public ProductCodeDTO validateProductCode(@RequestBody Map<String, Object> productCode) {
    return productService.validateProductCode((String) productCode.get("productCode")).orElse(null);
  }

  @PostMapping("/registerCustomer")
  public void registerCustomer(@RequestBody CustomerDTO customerDTO) {
    User user = userService.registerCustomer(customerDTO);
    mailService.sendActivationEmail(user);
  }

  @GetMapping("/activate")
  public void activateAccount(@RequestParam(value = "key") String key) {
    Optional<User> user = userService.activateRegistration(key);
    if (!user.isPresent()) {
      throw new AccountResourceException("No user was found for this activation key");
    }
  }

  @GetMapping("/authenticate")
  public String isAuthenticated(HttpServletRequest request) {
    log.debug("REST request to check if the current user is authenticated");
    return request.getRemoteUser();
  }

  @GetMapping("/account")
  public UserDTO getAccount() {
    return userService.getUserWithAuthorities()
      .map(UserDTO::new)
      .orElseThrow(() -> new AccountResourceException("User could not be found"));
  }

  @PostMapping("/account")
  public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
    String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
      throw new EmailAlreadyUsedException();
    }
    Optional<User> user = userRepository.findOneByLogin(userLogin);
    if (!user.isPresent()) {
      throw new AccountResourceException("User could not be found");
    }
    userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
      userDTO.getLangKey(), userDTO.getImageUrl());
  }

  @PostMapping(path = "/account/change-password")
  public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
    if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
      throw new InvalidPasswordException();
    }
    userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
  }


  @PostMapping(path = "/account/reset-password/start")
  public void requestPasswordReset(@RequestBody String mail) {
    mailService.sendPasswordResetMail(
      userService.requestPasswordReset(mail)
        .orElseThrow(EmailNotFoundException::new)
    );
  }

  @PostMapping(path = "/account/forgot-username")
  public void forgotUsername(@RequestBody String mail) {
    mailService.sendUsernameEmail(
      userService.getUserByEmail(mail)
        .orElseThrow(EmailNotFoundException::new)
    );
  }

  @PostMapping(path = "/account/reset-password/finish")
  public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
    if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
      throw new InvalidPasswordException();
    }
    Optional<User> user =
      userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

    if (!user.isPresent()) {
      throw new AccountResourceException("No user was found for this reset key");
    }
  }

  private static boolean checkPasswordLength(String password) {
    return !StringUtils.isEmpty(password) &&
      password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
      password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
  }
}
