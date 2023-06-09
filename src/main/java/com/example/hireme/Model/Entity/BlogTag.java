package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tags")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BlogTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @ManyToMany
    @JoinTable(name = "blogs_tags", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "blog_id"))
    List<Blog> blogs;

    public BlogTag(String label, List<Blog> blogs) {
        this.label = label;
        this.blogs = blogs;
    }

    public BlogTag(String label) {
        this.label = label;
    }
}
