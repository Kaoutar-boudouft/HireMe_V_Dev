package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "admins_profiles")
@NoArgsConstructor
@Data
@Setter
@Getter
public class AdminProfile extends Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public AdminProfile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number, User user, City city) {
        super(first_name, last_name, birth_date, id_number, mobile_number, user, city);
    }

    public AdminProfile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
    }
}
