package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
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
    private Boolean Active;

    @OneToMany(mappedBy="country")
    private List<City> cities;

    public Country(String name, Boolean active, List<City> cities) {
        this.name = name;
        Active = active;
        this.cities = cities;
    }

    public Country(String name, Boolean active) {
        this.name = name;
        Active = active;
    }
}
