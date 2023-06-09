package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    Media findByEntityAndEntityIdAndType(String entity ,Long entity_id ,String type);
}
