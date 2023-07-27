package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.User;
import com.example.hireme.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    List<User> findByRole(Role role);

    Integer countAllByRole(Role role);
}
