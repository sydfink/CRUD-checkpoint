package com.galvanize.CRUDcheckpoint;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {


    User findUserByEmail(String email);
}