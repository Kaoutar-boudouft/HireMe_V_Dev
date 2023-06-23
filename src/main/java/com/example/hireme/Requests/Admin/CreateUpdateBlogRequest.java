package com.example.hireme.Requests.Admin;

import com.example.hireme.Model.Entity.BlogTag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUpdateBlogRequest {

    Long id;

    @NotEmpty()
    @Size(min = 5,max = 30)
    String title;

    @NotEmpty()
    @Size(min = 20)
    String content;

    @NotEmpty()
    String language;

    List<Long> tags_id;

    @NotNull
    MultipartFile file;

    public CreateUpdateBlogRequest(Long id,String title, String content, String language, List<Long> blogTags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.language = language;
        this.tags_id = blogTags;
    }

    public CreateUpdateBlogRequest(String title, String content, String language, MultipartFile file) {
        this.title = title;
        this.content = content;
        this.language = language;
        this.file = file;
    }

    public CreateUpdateBlogRequest(String title, String content, String language) {
        this.title = title;
        this.content = content;
        this.language = language;
    }
}
