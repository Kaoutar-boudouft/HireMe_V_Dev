package com.example.hireme.Service;

import com.example.hireme.Model.Entity.NewsLetter;
import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Repository.NewsLetterRepository;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import com.example.hireme.Requests.Admin.CreateUpdateNewsLetterRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NewsLetterService {
    private final NewsLetterRepository newsLetterRepository;

    public List<NewsLetter> getAll(){
        return newsLetterRepository.findAll();
    }

    public Optional<NewsLetter> findById(Long news_id){
        return newsLetterRepository.findById(news_id);
    }

    public NewsLetter create(CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest){
        NewsLetter newsLetter = new NewsLetter();
        newsLetter.setEmail(createUpdateNewsLetterRequest.getEmail());
        return newsLetterRepository.save(newsLetter);
    }
    public NewsLetter update(CreateUpdateNewsLetterRequest createUpdateNewsLetterRequest){
        NewsLetter newsLetter = newsLetterRepository.getReferenceById(createUpdateNewsLetterRequest.getId());
        newsLetter.setEmail(createUpdateNewsLetterRequest.getEmail());
        return newsLetterRepository.save(newsLetter);
    }

    public Boolean checkEmailExistance(String email){
        Optional<NewsLetter> newsLetter = newsLetterRepository.findByEmail(email);
        return newsLetter.isPresent();
    }

    public void remove(NewsLetter newsLetter){
        newsLetterRepository.delete(newsLetter);
    }

}
