package com.example.hireme.Controller.Admin;

import com.example.hireme.Model.Entity.Blog;
import com.example.hireme.Model.Entity.BlogTag;
import com.example.hireme.Model.Entity.Media;
import com.example.hireme.Model.Entity.User;
import com.example.hireme.MultiLanguages.LanguageConfig;
import com.example.hireme.Repository.BlogTagRepository;
import com.example.hireme.Repository.JobOfferRepository;
import com.example.hireme.Requests.Admin.CreateUpdateBlogRequest;
import com.example.hireme.Requests.Admin.CreateUpdateTagRequest;
import com.example.hireme.Service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/admin/blogs")
public class BlogManagementController {

    private final JobOfferService jobOfferService;
    private final BlogTagService blogTagService;
    private final BlogService blogService;
    private final MediaService mediaService;
    private final JobOfferRepository jobOfferRepository;
    private final LanguageConfig languageConfig;
    private final BlogTagRepository blogTagRepository;
    private final FileUploadService fileUploadService;
    private final EmployerProfileService employerProfileService;

    @GetMapping()
    public String getBlogsPage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        List<Blog> blogs = blogService.getAll();
        model.addAttribute("user",user);
        model.addAttribute("blogs",blogs);
        model.addAttribute("type","dashboard");
        return "Admin/blogs";
    }

    @GetMapping("/{blog_id}/edit")
    public String getTagsUpdatePage(@PathVariable("blog_id") Long blog_id, Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
        Optional<Blog> blog = blogService.findById(blog_id);
        List<BlogTag> tags = blogTagService.getAll();
        if (blog.isPresent()){
            CreateUpdateBlogRequest createUpdateBlogRequest = blogService.prepareCreateUpdateBlogRequest(blog.get());
            Media media = mediaService.getMedia("Blog",blog_id,"blog");
            model.addAttribute("user",user);
            model.addAttribute("createUpdateBlogRequest",createUpdateBlogRequest);
            model.addAttribute("type","dashboard");
            model.addAttribute("media",media);
            model.addAttribute("blog_id",blog_id);
            model.addAttribute("tags",tags);
            return "Admin/update_create_blog";
        }
        return "redirect:/admin/blogs";
    }

   @GetMapping("/create")
    public String getBlogCreatePage(Authentication authentication, Model model){
        User user = (User) authentication.getPrincipal();
            CreateUpdateBlogRequest createUpdateBlogRequest = new CreateUpdateBlogRequest();
       List<BlogTag> tags = blogTagService.getAll();
       model.addAttribute("user",user);
            model.addAttribute("createUpdateBlogRequest",createUpdateBlogRequest);
            model.addAttribute("type","dashboard");
            model.addAttribute("media",null);
            model.addAttribute("blog_id",null);
            model.addAttribute("tags",tags);
       return "Admin/update_create_blog";
    }

    @PostMapping("/store")
    public String createBlog(Authentication authentication , @Valid CreateUpdateBlogRequest createUpdateBlogRequest,
                            BindingResult bindingResult, RedirectAttributes redirectAttributes, Locale locale, Model model) {
        User user = (User) authentication.getPrincipal();
        List<BlogTag> tags = blogTagService.getAll();
        model.addAttribute("media",null);
        model.addAttribute("user",user);
        model.addAttribute("type","dashboard");
        model.addAttribute("blog_id",null);
        model.addAttribute("tags",tags);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", bindingResult);
            redirectAttributes.addFlashAttribute("createUpdateBlogRequest", createUpdateBlogRequest);
            return "Admin/update_create_blog";
        }
        Blog blog =blogService.create(createUpdateBlogRequest);
        try {
            Media checkMedia = mediaService.getMedia("Blog",blog.getId(), "blog");
            Media media;
            if (checkMedia != null) {
                media = checkMedia;
            } else {
                media = new Media("Blog",blog.getId(), "blog");
            }
            fileUploadService.uploadFile(createUpdateBlogRequest.getFile(), media, locale);
            for (int i=0;i<createUpdateBlogRequest.getTags_id().size();i++){
                System.out.println("kaoutar"+createUpdateBlogRequest.getTags_id().get(i));
                jobOfferRepository.linkBlogWithTag(blog.getId(),createUpdateBlogRequest.getTags_id().get(i));
            }
        } catch (IOException e) {
            model.addAttribute("file_error", e.getMessage());
            redirectAttributes.addFlashAttribute("createUpdateBlogRequest", createUpdateBlogRequest);
            return "Admin/update_create_blog";
        }
           redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
           return "redirect:/admin/blogs";
    }

    @PostMapping("/{blog_id}/update")
    public String updateJob(Authentication authentication,@PathVariable("blog_id") Long blog_id,@Valid CreateUpdateBlogRequest createUpdateBlogRequest,
                            BindingResult bindingResult,RedirectAttributes redirectAttributes,Model model,
                            Locale locale){
        User user = (User) authentication.getPrincipal();
        Optional<Blog> blog = blogService.findById(blog_id);
        if (blog.isPresent()){
            List<BlogTag> tags = blogTagService.getAll();
            model.addAttribute("media",null);
            model.addAttribute("user",user);
            model.addAttribute("type","dashboard");
            model.addAttribute("blog_id",blog_id);
            model.addAttribute("tags",tags);
                if (bindingResult.hasErrors()){
                    redirectAttributes.addFlashAttribute("error", bindingResult);
                    redirectAttributes.addFlashAttribute("createUpdateBlogRequest", createUpdateBlogRequest);
                    return "Admin/update_create_blog";
                }
                createUpdateBlogRequest.setId(blog_id);
            blogService.update(createUpdateBlogRequest);
            try {
                Media checkMedia = mediaService.getMedia("Blog",blog.get().getId(), "blog");
                Media media;
                if (checkMedia != null) {
                    media = checkMedia;
                } else {
                    media = new Media("Blog",blog.get().getId(), "blog");
                }
                fileUploadService.uploadFile(createUpdateBlogRequest.getFile(), media, locale);
            } catch (IOException e) {
                model.addAttribute("file_error", e.getMessage());
                redirectAttributes.addFlashAttribute("createUpdateBlogRequest", createUpdateBlogRequest);
                return "Admin/update_create_blog";
            }
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("create",new Object[] {}, locale));
            return "redirect:/admin/blogs/"+blog_id+"/edit";
        }
        else {
            return "redirect:/admin/blogs";
        }
    }

    @GetMapping("/delete/{blog_id}")
    public String deleteCompany(@PathVariable("blog_id") Long tag_id,
                                RedirectAttributes redirectAttributes,Locale locale,Model model){
        Optional<Blog> blog = blogService.findById(tag_id);
        if (blog.isPresent()){
            mediaService.deleteMedia(new Media("Blog",blog.get().getId(),"blog"));
            blogService.removeLinkbetweenBlogAndTag(blog.get());
            blogService.remove(blog.get());
            redirectAttributes.addFlashAttribute("successMessage",languageConfig.messageSource().getMessage("update",new Object[] {}, locale));
        }
        return "redirect:/admin/blogs";
    }


}
