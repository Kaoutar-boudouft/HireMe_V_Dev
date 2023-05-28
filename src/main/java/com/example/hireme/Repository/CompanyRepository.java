package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Company;
import com.example.hireme.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    @Query(value = "select * from users u where u.user_id=?1", nativeQuery = true)
    public List<User> findByUserId(Long id);}
