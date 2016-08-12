package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.RoleDTO;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.dto.UserPasswordDTO;
import com.nixmash.springdata.jpa.enums.ResetPasswordResult;
import com.nixmash.springdata.jpa.enums.Role;
import com.nixmash.springdata.jpa.model.*;
import com.nixmash.springdata.jpa.repository.AuthorityRepository;
import com.nixmash.springdata.jpa.repository.UserConnectionRepository;
import com.nixmash.springdata.jpa.repository.UserRepository;
import com.nixmash.springdata.jpa.repository.UserTokenRepository;
import com.nixmash.springdata.jpa.utils.UserUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final UserConnectionRepository userConnectionRepository;
    private final UserTokenRepository userTokenRepository;


    @PersistenceContext
    private EntityManager em;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository,
                           UserConnectionRepository userConnectionRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.userConnectionRepository = userConnectionRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(long id) {
        logger.debug("Getting user={}", id);
        return Optional.ofNullable(userRepository.findById(id));
    }

    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        logger.debug("Getting user={}", username);
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getByEmail(String email) {
        logger.debug("Getting user by email={}", email);
        return userRepository.findOneByEmail(email);
    }

    @Transactional(readOnly = true)
    public Collection<User> getAllUsers() {
        logger.debug("Getting all users");
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User create(UserDTO form) {

        User user = new User();
        user.setUsername(form.getUsername());
        user.setFirstName(form.getFirstName());
        user.setLastName(form.getLastName());
        user.setEmail(form.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        user.setUserKey(RandomStringUtils.randomAlphanumeric(16));
        user.setSignInProvider(form.getSignInProvider());
        User saved = userRepository.save(user);

        for (Authority authority : form.getAuthorities()) {
            Authority _authority = authorityRepository.findByAuthority(authority.getAuthority());
            saved.getAuthorities().add(_authority);
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Authority> getRoles() {
        return authorityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsersWithDetail() {
        return userRepository.getUsersWithDetail();
    }

    @Override
    public boolean canAccessUser(CurrentUser currentUser, String username) {
        logger.info("Checking if user={} has access to user={}", currentUser, username);
        return currentUser != null
                && (currentUser.getUser().hasAuthority(Role.ROLE_ADMIN) ||
                currentUser.getUsername().equals(username));
    }

    @Transactional(readOnly = true)
    @Override
    public UserConnection getUserConnectionByUserId(String userId) {
        logger.debug("Getting userConnection={}", userId);
        return userConnectionRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public ResetPasswordResult updatePassword(UserPasswordDTO userPasswordDTO) {
        boolean isLoggedIn = userPasswordDTO.getUserId() > 0;
        User user = null;
        Optional<UserToken> userToken = Optional.empty();
        if (isLoggedIn)
            user = userRepository.findById(userPasswordDTO.getUserId());
        else {
            userToken =
                    userTokenRepository.findByToken(userPasswordDTO.getVerificationToken());

            if (userToken.isPresent()) {
                user = userToken.get().getUser();
                if (!isValidToken(user.getId(), userToken.get().getToken())) {
                    user = null;
                }
            }
        }

        if (user == null)
            return ResetPasswordResult.ERROR;
        else {
            user.setPassword(UserUtils.bcryptedPassword(userPasswordDTO.getPassword()));
            if (userToken.isPresent()) {
                userTokenRepository.delete(userToken.get());
            }
        }

        if (isLoggedIn)
            return ResetPasswordResult.LOGGEDIN_SUCCESSFUL;
        else
            return ResetPasswordResult.FORGOT_SUCCESSFUL;
    }

    @Transactional
    @Override
    public UserToken createUserToken(User user) {
        Optional<UserToken> userToken = userTokenRepository.findByUserId(user.getId());
        if (userToken.isPresent())
            userToken.get().updateToken(UUID.randomUUID().toString());
        else
            userToken = Optional.of(new UserToken(UUID.randomUUID().toString(), user));

        return userTokenRepository.save(userToken.get());
    }

    @Transactional
    @Override
    public Optional<UserToken> getUserToken(String token) {
        return userTokenRepository.findByToken(token);
    }

    @Transactional
    @Override
    public User update(UserDTO userDTO) {

        User user = userRepository.findById(userDTO.getUserId());
        user.update(userDTO.getUsername(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail());
        if (userDTO.isUpdateChildren()) {

            user.getAuthorities().clear();
            for (Authority authority : userDTO.getAuthorities()) {
                Authority match = authorityRepository.findOne(authority.getId());
                if (!user.getAuthorities().contains(match)) {
                    user.getAuthorities().add(match);
                }
            }
        }
        return user;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getUserByIdWithDetail(Long ID) {
        return userRepository.findByUserIdWithDetail(ID);
    }

    // region Authorities

    @Transactional
    @Override
    public Authority createAuthority(RoleDTO roleDTO) {
        Authority authority = new Authority();
        authority.setAuthority(roleDTO.getAuthority());
        return authorityRepository.save(authority);
    }

    @Transactional
    @Override
    public Authority updateAuthority(RoleDTO roleDTO) {
        Authority authority = authorityRepository.findOne(roleDTO.getId());
        authority.setAuthority(roleDTO.getAuthority());
        return authority;
    }

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }

    @Transactional
    @Override
    public void deleteAuthority(Authority authority, Collection<User> users) {
        if (users != null) {
            for (User user : users) {
                user.getAuthorities().remove(authority);
            }
        }
        authorityRepository.delete(authority);
    }

    @Override
    public Collection<User> getUsersByAuthorityId(Long authorityId) {
        return userRepository.findByAuthorityId(authorityId);

    }

    @Transactional
    @Override
    public User updateHasAvatar(Long userId, boolean hasAvatar) {
        User user = userRepository.findById(userId);
        user.setHasAvatar(hasAvatar);

        CurrentUser currentUser = new CurrentUser(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        currentUser,
                        user.getPassword(),
                        user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return user;
    }
    // endregion

    // region Utility methods

    private Boolean isValidToken(long userId, String token) {
        final Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        boolean isValidToken = false;
        if (userToken.isPresent()) {
            final Calendar cal = Calendar.getInstance();
            UserToken passToken = userToken.get();

            if (passToken.getUser().getId().equals(userId) && (passToken.getTokenExpiration().getTime() - cal.getTime().getTime()) > 0) {
                isValidToken = true;
            }
        }
        return isValidToken;
    }

    // endregion
}


