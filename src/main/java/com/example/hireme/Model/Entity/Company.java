package com.example.hireme.Model.Entity;

import com.example.hireme.Model.CompanyPriority;
import com.example.hireme.Model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String fiscal_id;
    private Integer phone_number;
    private String email;
    private String website;

    @Enumerated(value = EnumType.STRING)
    private CompanyPriority priority;

    private String address;
    private Boolean active;

    private LocalDateTime created_at;

    @OneToOne(mappedBy = "company")
    @PrimaryKeyJoinColumn
    private EmployerProfile employerProfile;

    @OneToMany(mappedBy="company")
    private List<JobOffer> jobs_offers;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=true)
    private City city;

    public Company(String name, String fiscal_id, Integer phone_number, String email, String website, CompanyPriority priority, String address, Boolean active, LocalDateTime created_at, EmployerProfile employerProfile, City city) {
        this.name = name;
        this.fiscal_id = fiscal_id;
        this.phone_number = phone_number;
        this.email = email;
        this.website = website;
        this.priority = priority;
        this.address = address;
        this.active = active;
        this.created_at = created_at;
        this.employerProfile = employerProfile;
        this.city = city;
    }

    public Company(String name, String fiscal_id, Integer phone_number, String email, String website, CompanyPriority priority, String address, Boolean active, LocalDateTime created_at) {
        this.name = name;
        this.fiscal_id = fiscal_id;
        this.phone_number = phone_number;
        this.email = email;
        this.website = website;
        this.priority = priority;
        this.address = address;
        this.active = active;
        this.created_at = created_at;
    }
}
