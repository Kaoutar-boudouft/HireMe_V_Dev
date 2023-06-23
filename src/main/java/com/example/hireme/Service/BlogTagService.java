package com.example.hireme.Service;

import com.example.hireme.Model.Entity.Blog;
import com.example.hireme.Model.Entity.BlogTag;
import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Repository.BlogTagRepository;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import com.example.hireme.Requests.Admin.CreateUpdateTagRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlogTagService {
    private final BlogTagRepository blogTagRepository;

    public List<BlogTag> getAll(){
        return blogTagRepository.findAll();
    }

    public Optional<BlogTag> findById(Long tag_id){
        return blogTagRepository.findById(tag_id);
    }

    public Boolean checkTagExistance(String label){
        return blogTagRepository.findByLabel(label).isPresent();
    }

    public BlogTag create(CreateUpdateTagRequest createUpdateTagRequest){
        BlogTag blogTag = new BlogTag();
        blogTag.setLabel(createUpdateTagRequest.getLabel());
        return blogTagRepository.save(blogTag);
    }

    public BlogTag update(CreateUpdateTagRequest createUpdateTagRequest){
        BlogTag blogTag = blogTagRepository.getReferenceById(createUpdateTagRequest.getId());
        blogTag.setLabel(createUpdateTagRequest.getLabel());
        return blogTagRepository.save(blogTag);
    }

    public BlogTag removeBlogsLLinks(BlogTag blogTag){
        List<Blog> blogs = new ArrayList<>();
        blogTag.setBlogs(blogs);
        return blogTagRepository.save(blogTag);
    }

    public void remove(BlogTag blogTag){
        blogTagRepository.delete(blogTag);
    }
}
