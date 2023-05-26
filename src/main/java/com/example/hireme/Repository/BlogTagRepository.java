package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.BlogTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogTagRepository extends JpaRepository<BlogTag,Long> {
}
