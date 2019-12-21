package com.warmachine.errorcenterapi.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warmachine.errorcenterapi.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailEquals(String email);

}
