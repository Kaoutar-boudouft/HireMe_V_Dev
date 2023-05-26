package com.example.hireme.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="offers_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
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
