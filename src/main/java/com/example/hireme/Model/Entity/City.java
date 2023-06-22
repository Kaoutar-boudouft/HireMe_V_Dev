package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="cities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int active;

    @OneToMany(mappedBy="city")
    @JsonIgnore
    private List<Profile> profiles;

    @OneToMany(mappedBy="city")
    @JsonIgnore
    private List<JobOffer> jobs_offers;

    @OneToMany(mappedBy="city")
    @JsonIgnore
    private List<Company> companies;

    @ManyToOne
    @JoinColumn(name="country_id", nullable=false)
    private Country country;

    public City(String name, int active) {
        this.name = name;
        active = active;
    }
}
