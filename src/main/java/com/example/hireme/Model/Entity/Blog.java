package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Language;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="blogs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @Enumerated(value = EnumType.STRING)
    private Language language;

    @ManyToMany(mappedBy = "blogs")
    List<BlogTag> tags;

    public Blog(String title, String content, Language language, List<BlogTag> tags) {
        this.title = title;
        this.content = content;
        this.language = language;
        this.tags = tags;
    }

    public Blog(String title, String content, Language language) {
        this.title = title;
        this.content = content;
        this.language = language;
    }
}
