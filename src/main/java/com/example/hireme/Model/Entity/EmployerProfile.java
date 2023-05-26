package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employers_profiles")
@NoArgsConstructor
public class EmployerProfile extends Profile {

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public EmployerProfile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number, User user, City city, Company company) {
        super(first_name, last_name, birth_date, id_number, mobile_number, user, city);
        this.company = company;
    }

    public EmployerProfile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number, Company company) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
        this.company = company;
    }
}
