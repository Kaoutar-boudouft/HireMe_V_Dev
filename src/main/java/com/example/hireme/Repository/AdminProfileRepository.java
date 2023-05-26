package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.AdminProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProfileRepository extends JpaRepository<AdminProfile,Long> {
}
