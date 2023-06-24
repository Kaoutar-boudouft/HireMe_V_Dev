package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Blog;
import com.example.hireme.Model.Language;
import com.example.hireme.Repository.BlogRepository;
import com.example.hireme.Requests.Admin.CreateUpdateBlogRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Blog> getAll(){
        return blogRepository.findAll();
    }

    public Optional<Blog> findById(Long blog_id){
        return blogRepository.findById(blog_id);
    }

    public CreateUpdateBlogRequest prepareCreateUpdateBlogRequest(Blog blog){
        List<Long> tags_id = new ArrayList<>();
        for(int i=0;i<blog.getTags().size();i++){
            tags_id.add(blog.getTags().get(i).getId());
        }
        return new CreateUpdateBlogRequest(
                blog.getId(), blog.getTitle(), blog.getContent(), blog.getLanguage().name(), tags_id
        );
    }

   public Blog create(CreateUpdateBlogRequest createUpdateBlogRequest){
        Blog blog = new Blog();
        blog.setTitle(createUpdateBlogRequest.getTitle());
        blog.setContent(createUpdateBlogRequest.getContent());
        blog.setLanguage(Language.valueOf(createUpdateBlogRequest.getLanguage()));
        return blogRepository.save(blog);
    }

    public Blog update(CreateUpdateBlogRequest createUpdateBlogRequest){
        Blog blog = blogRepository.getReferenceById(createUpdateBlogRequest.getId());
        blog.setTitle(createUpdateBlogRequest.getTitle());
        blog.setContent(createUpdateBlogRequest.getContent());
        blog.setLanguage(Language.valueOf(createUpdateBlogRequest.getLanguage()));
        removeLinkbetweenBlogAndTag(blog);
        for (int i=0;i<createUpdateBlogRequest.getTags_id().size();i++){
            blogRepository.linkBlogWithTag(blog.getId(),createUpdateBlogRequest.getTags_id().get(i));
        }
        return blogRepository.save(blog);
    }

    public void linkBlogWithTags(Long blog_id,Long tag_id){
        blogRepository.linkBlogWithTag(blog_id,tag_id);
    }

    public void removeLinkbetweenBlogAndTag(Blog blog){
        blogRepository.removeLinkbetweenBlogAndTag(blog.getId());
    }

    public void remove(Blog blog){
        blogRepository.delete(blog);
    }

    public List<Blog> findRecentBlogs(String language){
        return blogRepository.findRecentBlogs(language);
    }

}
