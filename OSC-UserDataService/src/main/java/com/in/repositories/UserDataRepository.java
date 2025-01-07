package com.in.repositories;
import com.in.dtos.LoginDTO;
import com.in.entities.UserEntity;
import jakarta.transaction.Transactional;
import  org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserEntity, String>{
    boolean existsByEmail(String email);

    @Query("SELECT new com.in.dtos.LoginDTO(u.userId, u.name, u.password) FROM UserEntity u WHERE u.userId = :userId")
    LoginDTO findNameAndPasswordByUserId(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.password = :password WHERE u.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String password);

}