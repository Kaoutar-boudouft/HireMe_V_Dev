package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="countries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String region;
    private String currency;
    private int active;

    @OneToMany(mappedBy="country")
    @JsonIgnore
    private List<City> cities;

    public Country(String name, int active, List<City> cities) {
        this.name = name;
        this.active = active;
        this.cities = cities;
    }

    public Country(String name, int active) {
        this.name = name;
        this.active = active;
    }
}
