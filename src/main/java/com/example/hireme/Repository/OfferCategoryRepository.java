package com.example.hireme.Repository;

import com.example.hireme.Model.Entity.OfferCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfferCategoryRepository extends JpaRepository<OfferCategory,Long> {
    Optional<OfferCategory> findByLabel(String label);
}
