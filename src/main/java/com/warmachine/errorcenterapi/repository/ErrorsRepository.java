package com.warmachine.errorcenterapi.repository;

import com.warmachine.errorcenterapi.entity.Error;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorsRepository extends JpaRepository<Error, Long> {
    
}
