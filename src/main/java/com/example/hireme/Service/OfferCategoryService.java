package com.example.hireme.Service;

import com.example.hireme.Model.Entity.OfferCategory;
import com.example.hireme.Repository.OfferCategoryRepository;
import com.example.hireme.Requests.Admin.CreateUpdateCategoryRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OfferCategoryService {
    private final OfferCategoryRepository offerCategoryRepository;

    public List<OfferCategory> getAllCategories(){
        return offerCategoryRepository.findAll();
    }
    public Long countAll(){
        return offerCategoryRepository.count();
    }

    public Optional<OfferCategory> findById(Long category_id){
        return offerCategoryRepository.findById(category_id);
    }

    public OfferCategory create(CreateUpdateCategoryRequest createUpdateCategoryRequest){
        OfferCategory offerCategory = new OfferCategory();
        offerCategory.setLabel(createUpdateCategoryRequest.getLabel());
        return offerCategoryRepository.save(offerCategory);
    }

    public OfferCategory update(CreateUpdateCategoryRequest createUpdateCategoryRequest){
        OfferCategory offerCategory = offerCategoryRepository.getReferenceById(createUpdateCategoryRequest.getId());
        offerCategory.setLabel(createUpdateCategoryRequest.getLabel());
        return offerCategoryRepository.save(offerCategory);
    }

    public Boolean checkCategoryExistance(String label){
        Optional<OfferCategory> offerCategory = offerCategoryRepository.findByLabel(label);
        return offerCategory.isPresent();
    }

    public void remove(OfferCategory offerCategory){
        offerCategoryRepository.delete(offerCategory);
    }

}
