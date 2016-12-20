package com.nixmash.springdata.jpa.repository;

import com.nixmash.springdata.jpa.model.UserData;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by daveburke on 12/19/16.
 */
public interface UserDataRepository extends CrudRepository<UserData, Long>{

}
