package com.crm.ehelpdesk.dto;

import com.crm.ehelpdesk.config.constants.Constants;
import com.crm.ehelpdesk.domain.User;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(max = 10)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private String authority;

    private String owner;

    private Set<AvailableStatesDTO> accessibleStates;

    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserDTO(User user, List<AvailableStatesDTO> accessibleStates) {
      setUserDto(user, accessibleStates);
    }

  private void setUserDto(User user, List<AvailableStatesDTO> accessibleStates) {
    this.id = user.getId();
    this.login = user.getLogin();
    this.firstName = user.getFirstName();
    this.lastName = user.getLastName();
    this.email = user.getEmail();
    this.activated = user.getActivated();
    this.imageUrl = user.getImageUrl();
    this.langKey = user.getLangKey();
    this.createdBy = user.getCreatedBy();
    this.createdDate = user.getCreatedDate();
    this.lastModifiedBy = user.getLastModifiedBy();
    this.lastModifiedDate = user.getLastModifiedDate();
    this.authority = user.getAuthorities().getName();
    this.owner = user.getOwner();
    if (!CollectionUtils.isEmpty(accessibleStates)) {
      this.accessibleStates = new HashSet<>(accessibleStates);
    }
  }

  public UserDTO(User user) {
    setUserDto(user, new ArrayList<>());
  }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Set<AvailableStatesDTO> getAccessibleStates() {
    return accessibleStates;
  }

  public void setAccessibleStates(Set<AvailableStatesDTO> accessibleStates) {
    this.accessibleStates = accessibleStates;
  }

  @Override
    public String toString() {
        return "UserDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            ", authority=" + authority +
            "}";
    }
}
