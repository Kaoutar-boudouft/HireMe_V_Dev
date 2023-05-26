package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean Active;

    @OneToMany(mappedBy="city")
    private List<Profile> profiles;

    @OneToMany(mappedBy="city")
    private List<JobOffer> jobs_offers;

    @OneToMany(mappedBy="city")
    private List<Company> companies;

    @ManyToOne
    @JoinColumn(name="country_id", nullable=false)
    private Country country;

    public City(String name, Boolean active) {
        this.name = name;
        Active = active;
    }
}
