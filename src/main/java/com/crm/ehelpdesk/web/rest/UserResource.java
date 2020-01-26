package com.crm.ehelpdesk.web.rest;

import com.crm.ehelpdesk.config.constants.Constants;
import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import com.crm.ehelpdesk.config.util.HeaderUtil;
import com.crm.ehelpdesk.config.util.PaginationUtil;
import com.crm.ehelpdesk.config.util.ResponseUtil;
import com.crm.ehelpdesk.dao.repository.UserRepository;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.AvailableStatesDTO;
import com.crm.ehelpdesk.dto.UserDTO;
import com.crm.ehelpdesk.exception.BadRequestAlertException;
import com.crm.ehelpdesk.exception.EmailAlreadyUsedException;
import com.crm.ehelpdesk.exception.LoginAlreadyUsedException;
import com.crm.ehelpdesk.exception.OwnerNotFoundException;
import com.crm.ehelpdesk.web.service.MailService;
import com.crm.ehelpdesk.web.service.MasterService;
import com.crm.ehelpdesk.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserResource {

  private final Logger log = LoggerFactory.getLogger(UserResource.class);

  @Value("${ehelpdesk.ehelpdesk-app.name}")
  private String applicationName;

  private final UserService userService;

  private final UserRepository userRepository;

  private final MailService mailService;

  private final MasterService masterService;

  public UserResource(UserService userService, UserRepository userRepository, MailService mailService, MasterService masterService) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.mailService = mailService;
    this.masterService = masterService;
  }

  @PostMapping("/users")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SUPER_ADMIN + "\")")
  public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
    log.debug("REST request to save User : {}", userDTO);

    if (userDTO.getId() != null) {
      throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
    } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
      throw new LoginAlreadyUsedException();
    } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
      throw new EmailAlreadyUsedException();
    } else if (userRepository.findOneByLogin(userDTO.getOwner().toLowerCase()).isPresent()) {
      User newUser = userService.createUser(userDTO);
      if (AuthoritiesConstants.ADMIN.equals(userDTO.getAuthority())) {
        masterService.createDBAccessDetails(userDTO.getAccessibleStates(), newUser.getId());
      }
      mailService.sendCreationEmail(newUser);
      return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
        .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getLogin()))
        .body(newUser);
    } else {
      throw new OwnerNotFoundException();
    }
  }

  @PutMapping("/users")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SUPER_ADMIN + "\")")
  public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
    log.debug("REST request to update User : {}", userDTO);
    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
      throw new EmailAlreadyUsedException();
    }
    existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
    if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
      throw new LoginAlreadyUsedException();
    }
    if (userRepository.findOneByLogin(userDTO.getOwner().toLowerCase()).isPresent()) {
      Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
      if (AuthoritiesConstants.ADMIN.equals(userDTO.getAuthority()) && updatedUser.isPresent()) {
        masterService.updateDBAccessDetails(userDTO.getAccessibleStates(), updatedUser.get().getId());
      }
      return ResponseUtil.wrapOrNotFound(updatedUser,
        HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()));
    } else {
      throw new OwnerNotFoundException();
    }
  }

  @GetMapping("/users")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SUPER_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
    final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
      ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
  }

  @GetMapping("/users/authorities")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SUPER_ADMIN + "\")")
  public List<String> getAuthorities() {
    return userService.getAuthorities();
  }

  @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
    log.debug("REST request to get User : {}", login);
    Optional<User> user = userService.getUserWithAuthoritiesByLogin(login);
    List<AvailableStatesDTO> states = masterService.getAvailableStatesByUserId(user.get().getId());
    return ResponseUtil.wrapOrNotFound(Optional.of(new UserDTO(user.get(), states)));
  }

  @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SUPER_ADMIN + "\")")
  public ResponseEntity<Void> deleteUser(@PathVariable String login) {
    log.debug("REST request to delete User: {}", login);
    userService.deleteUser(login);
    return ResponseEntity
      .noContent().headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build();
  }
}
