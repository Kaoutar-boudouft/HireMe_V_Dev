package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
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
    private int Active;

    @OneToMany(mappedBy="city")
    private List<Profile> profiles;

    @OneToMany(mappedBy="city")
    private List<JobOffer> jobs_offers;

    @OneToMany(mappedBy="city")
    private List<Company> companies;

    @ManyToOne
    @JoinColumn(name="country_id", nullable=false)
    private Country country;

    public City(String name, int active) {
        this.name = name;
        Active = active;
    }
}
