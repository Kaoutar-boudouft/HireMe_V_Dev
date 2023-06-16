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
    @JoinColumn(name="company_id", nullable=false)
    private Company company;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=false)
    private City city;

    @ManyToMany(mappedBy = "job_offers",fetch = FetchType.LAZY)
    private List<CandidateProfile> candidates;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public JobType getType() {
        return type;
    }

    public void setType(JobType type) {
        this.type = type;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getPublished_at() {
        return published_at;
    }

    public void setPublished_at(LocalDateTime published_at) {
        this.published_at = published_at;
    }

    public OfferCategory getCategory() {
        return category;
    }

    public void setCategory(OfferCategory category) {
        this.category = category;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<CandidateProfile> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<CandidateProfile> candidates) {
        this.candidates = candidates;
    }

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
