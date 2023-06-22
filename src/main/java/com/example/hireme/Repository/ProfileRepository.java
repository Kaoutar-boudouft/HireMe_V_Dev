package com.example.hireme.Repository;

import com.example.hireme.Model.Profile;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@NoRepositoryBean
//@Primary
public interface ProfileRepository<T extends Profile> extends JpaRepository<T, Long> {

    T findByUserId(Long user_id);
    
}
