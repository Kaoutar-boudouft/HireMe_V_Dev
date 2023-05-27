package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employers_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class EmployerProfile extends Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    public EmployerProfile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, User user, City city, Company company) {
        super(first_name, last_name, birth_date, id_number, mobile_number, user, city);
        this.company = company;
    }

    public EmployerProfile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, Company company) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
        this.company = company;
    }
}
