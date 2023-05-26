package com.example.hireme.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="offers_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    @OneToMany(mappedBy="category")
    private List<JobOffer> jobs_offers;

    public OfferCategory(String label, List<JobOffer> jobs_offers) {
        this.label = label;
        this.jobs_offers = jobs_offers;
    }

    public OfferCategory(String label) {
        this.label = label;
    }
}
