package com.example.hireme.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;
    private String last_name;
    private LocalDateTime birth_date;
    private String id_number;
    private Integer mobile_number;

    public Profile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.birth_date = birth_date;
        this.id_number = id_number;
        this.mobile_number = mobile_number;
    }
}
