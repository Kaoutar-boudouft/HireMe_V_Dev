package com.example.hireme.Service;

import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Repository.OfferCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OfferCategoryService {
    private final OfferCategoryRepository offerCategoryRepository;

    public List<OfferCategory> getAllCategories(){
        return offerCategoryRepository.findAll();
    }

}
