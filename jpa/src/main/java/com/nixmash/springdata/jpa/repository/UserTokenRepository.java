package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.UserToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

    Optional<UserToken> findByToken(String token);

    Optional<UserToken> findByUserId(long userId);

}
