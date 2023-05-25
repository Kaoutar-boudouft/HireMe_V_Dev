package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Currency;
import com.example.hireme.Model.JobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
