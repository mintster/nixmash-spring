package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.RoleDTO;
import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.dto.UserPasswordDTO;
import com.nixmash.springdata.jpa.enums.ResetPasswordResult;
import com.nixmash.springdata.jpa.model.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getByEmail(String email);

    @Transactional(readOnly = true)
    Optional<User> getByUserKey(String userKey);

    Collection<User> getAllUsers();

    User create(UserDTO userDTO);

    User getUserByUsername(String username);

    @Transactional(readOnly = true)
    Collection<Authority> getRoles();

    List<User> getUsersWithDetail();

	boolean canAccessUser(CurrentUser currentUser, String username);

	UserConnection getUserConnectionByUserId(String userId);

    @Transactional
    ResetPasswordResult updatePassword(UserPasswordDTO userPasswordDTO);

    @Transactional
    UserToken createUserToken(User user);

    @Transactional
    Optional<UserToken> getUserToken(String token);

    User update(UserDTO userDTO);

    @Transactional(readOnly = true)
    Optional<User> getUserByIdWithDetail(Long ID);

    Authority createAuthority(RoleDTO roleDTO);

    Authority updateAuthority(RoleDTO roleDTO);

    Authority getAuthorityById(Long id);

    void deleteAuthority(Authority authority, Collection<User> users);

    Collection<User> getUsersByAuthorityId(Long authorityId);

    User updateHasAvatar(Long userId, boolean hasAvatar);

    boolean isValidToken(long userId, String token);
}
