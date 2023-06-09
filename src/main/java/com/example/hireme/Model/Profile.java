package com.example.hireme.Model;

import com.example.hireme.Model.Entity.City;
import com.example.hireme.Model.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Setter
@Getter
public abstract class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String first_name;
    protected String last_name;
    protected LocalDate birth_date;
    protected String id_number;
    protected Integer mobile_number;

    @OneToOne
    @JoinColumn(name = "user_id")
    protected User user;

    @ManyToOne
    @JoinColumn(name="city_id", nullable=true)
    protected City city;

    public Profile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, User user, City city) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.id_number = id_number;
        this.mobile_number = mobile_number;
        this.user = user;
        this.city = city;
    }

    public Profile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.id_number = id_number;
        this.mobile_number = mobile_number;
    }

    public Profile(String first_name, String last_name, LocalDate birth_date) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(LocalDate birth_date) {
        this.birth_date = birth_date;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public Integer getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(Integer mobile_number) {
        this.mobile_number = mobile_number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
