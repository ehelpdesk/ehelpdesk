package com.crm.ehelpdesk.web.service;

import com.crm.ehelpdesk.config.constants.Constants;
import com.crm.ehelpdesk.config.security.AuthoritiesConstants;
import com.crm.ehelpdesk.config.util.RandomUtil;
import com.crm.ehelpdesk.dao.repository.AuthorityRepository;
import com.crm.ehelpdesk.dao.repository.CustomerRepository;
import com.crm.ehelpdesk.dao.repository.PersistentTokenRepository;
import com.crm.ehelpdesk.dao.repository.UserRepository;
import com.crm.ehelpdesk.domain.Authority;
import com.crm.ehelpdesk.domain.Customer;
import com.crm.ehelpdesk.domain.User;
import com.crm.ehelpdesk.dto.CustomerDTO;
import com.crm.ehelpdesk.dto.UserDTO;
import com.crm.ehelpdesk.exception.EmailAlreadyUsedException;
import com.crm.ehelpdesk.exception.InvalidPasswordException;
import com.crm.ehelpdesk.exception.UsernameAlreadyUsedException;
import com.crm.ehelpdesk.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.crm.ehelpdesk.config.constants.CacheConstants.USERS_BY_EMAIL_CACHE;
import static com.crm.ehelpdesk.config.constants.CacheConstants.USERS_BY_LOGIN_CACHE;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PersistentTokenRepository persistentTokenRepository;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    private final CustomerRepository customerRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PersistentTokenRepository persistentTokenRepository, AuthorityRepository authorityRepository, CacheManager cacheManager, CustomerRepository customerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.persistentTokenRepository = persistentTokenRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
        this.customerRepository = customerRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
                .map(user -> {
                    user.setActivated(true);
                    user.setActivationKey(null);
                    this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    this.clearUserCaches(user);
                    return user;
                });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
                .filter(User::getActivated)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    this.clearUserCaches(user);
                    return user;
                });
    }

    public Optional<User> getUserByEmail(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
                .filter(User::getActivated);
    }

    public User registerUser(UserDTO userDTO, String password) {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorityRepository.findById(AuthoritiesConstants.CUSTOMER).ifPresent(newUser::setAuthorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (!StringUtils.isEmpty(userDTO.getAuthority())) {
            authorityRepository.findById(userDTO.getAuthority()).ifPresent(user::setAuthorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    user.setEmail(email.toLowerCase());
                    user.setLangKey(langKey);
                    user.setImageUrl(imageUrl);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                });
    }

    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
                .findById(userDTO.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(user -> {
                    this.clearUserCaches(user);
                    user.setLogin(userDTO.getLogin().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    user.setEmail(userDTO.getEmail().toLowerCase());
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    user.setLangKey(userDTO.getLangKey());
//        Set<Authority> managedAuthorities = user.getAuthorities();
//        managedAuthorities.clear();
//        userDTO.getAuthority().stream()
//          .map(authorityRepository::findById)
//          .filter(Optional::isPresent)
//          .map(Optional::get)
//          .forEach(managedAuthorities::add);
                    this.clearUserCaches(user);
                    log.debug("Changed Information for User: {}", user);
                    return user;
                })
                .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);
                    this.clearUserCaches(user);
                    log.debug("Changed password for User: {}", user);
                });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        List<String> userAuthorities = SecurityUtils.getUserAuthorities();
        return userRepository.findAllByLoginNot(pageable, AuthoritiesConstants.ADMIN).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(user -> {
                    log.debug("Deleting not activated user {}", user.getLogin());
                    userRepository.delete(user);
                    this.clearUserCaches(user);
                });
    }

    public List<String> getAuthorities() {
        List<String> userAuthorities = SecurityUtils.getUserAuthorities();
        if (userAuthorities.contains(AuthoritiesConstants.ADMIN)) {
            return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
        } else {
            return authorityRepository.findByNameNotIn(
                    new HashSet<>(Arrays.asList(AuthoritiesConstants.ADMIN, AuthoritiesConstants.MANAGER)))
                    .stream().map(Authority::getName).collect(Collectors.toList());
        }
    }


    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }

    @Transactional(readOnly = true)
    public Long getUserIdWithAuthoritiesByLogin() {
        return getUserWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin().get()).get().getId();
    }

    public User registerCustomer(CustomerDTO customerDTO) {
        UserDTO userDto = new UserDTO(customerDTO);
        registerUser(userDto, customerDTO.getPassword());
        Customer customer = new Customer();
        customer.setUsername(customerDTO.getUsername());
        customer.setProductCode(Long.parseLong(customerDTO.getProductCode()));
        customer.setAddress(customerDTO.getAddress());
        customerRepository.save(customer);
        return registerUser(userDto, customerDTO.getPassword());
    }

}
