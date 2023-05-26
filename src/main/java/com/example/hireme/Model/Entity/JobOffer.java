package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.JobType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="jobs_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JobOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double salary;

    @Enumerated(value = EnumType.STRING)
    private JobType type;

    @Enumerated(value = EnumType.STRING)
    private Currency currency;

    private Boolean active;

    private LocalDateTime published_at;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private OfferCategory category;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private City city;

    @ManyToMany(mappedBy = "job_offers")
    List<CandidateProfile> candidates;

    public JobOffer(String title, String description, Double salary, JobType type, Currency currency, Boolean active, LocalDateTime published_at, OfferCategory category, City city, List<CandidateProfile> candidates) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.type = type;
        this.currency = currency;
        this.active = active;
        this.published_at = published_at;
        this.category = category;
        this.city = city;
        this.candidates = candidates;
    }

    public JobOffer(String title, String description, Double salary, JobType type, Currency currency, Boolean active, LocalDateTime published_at) {
        this.title = title;
        this.description = description;
        this.salary = salary;
        this.type = type;
        this.currency = currency;
        this.active = active;
        this.published_at = published_at;
    }
}
