package com.nixmash.springdata.jpa.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.nixmash.springdata.jpa.dto.UserDTO;
import com.nixmash.springdata.jpa.model.CurrentUser;
import com.nixmash.springdata.jpa.model.User;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserDTO form);

    User getUserByUsername(String username);

    List<User> getUsersWithDetail();

//	boolean canAccessUser(String username);

	boolean canAccessUser(CurrentUser currentUser, String username);
}
