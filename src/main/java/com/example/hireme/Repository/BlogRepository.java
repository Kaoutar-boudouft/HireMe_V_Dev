package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.Blog;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    @Modifying
    @Transactional
    @Query(value = "insert into blogs_tags(tag_id,blog_id) VALUES(?2,?1)", nativeQuery = true)
    void linkBlogWithTag(Long blog_id,Long tag_id);

    @Modifying
    @Transactional
    @Query(value = "delete from blogs_tags where blog_id=?1", nativeQuery = true)
    void removeLinkbetweenBlogAndTag(Long blog_id);
}
