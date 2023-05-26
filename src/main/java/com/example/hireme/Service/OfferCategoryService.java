package com.example.hireme.Service;

import com.example.hireme.Repository.OfferCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OfferCategoryService {
    private final OfferCategoryRepository offerCategoryRepository;

}
