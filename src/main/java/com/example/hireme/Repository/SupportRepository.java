package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Support;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupportRepository extends JpaRepository<Support,Long> {
    Optional<Support> findByEmail(String email);
}
