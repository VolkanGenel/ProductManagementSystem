package com.volkan.repository;

import com.volkan.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    @Query(value = "select COUNT(a)>0 from Auth a where a.email=?1")
    boolean isEmail(String username);
    Optional<Auth> findOptionalByEmailIgnoreCaseAndPassword(String email, String password);
    Optional<Auth> findByEmail (String email);
}
