/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {

    User findByUsername(String username) throws DataAccessException;

    Collection<User> findAll() throws DataAccessException;

    User findById(Long id) throws DataAccessException;

    User save(User user) throws DataAccessException;

    User delete(User user) throws DataAccessException;

    boolean exists(Long userId) throws DataAccessException;

    @Query("select distinct u from User u left join fetch " +
            "u.authorities left join fetch u.userProfile p")
    List<User> getUsersWithDetail();

    @Query("select distinct u from User u left join fetch " +
            "u.authorities left join fetch u.userProfile p where u.id = ?1")
    Optional<User> findByUserIdWithDetail(Long ID);

    Optional<User> findOneByEmail(String email);

    @Query("select distinct u from User u left join u.authorities a where a.id = ?1")
    List<User> findByAuthorityId(Long id);

}
