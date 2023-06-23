package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.NewsLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsLetterRepository extends JpaRepository<NewsLetter,Long> {

    Optional<NewsLetter> findByEmail(String email);
}
