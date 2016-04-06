package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;
import com.nixmash.springdata.jpa.model.UserConnection;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserDTO form);

    User getUserByUsername(String username);

    List<User> getUsersWithDetail();

	boolean canAccessUser(CurrentUser currentUser, String username);

	UserConnection getUserConnectionByUserId(String userId);
}
