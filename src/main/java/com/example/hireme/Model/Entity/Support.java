package com.example.hireme.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="supports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    public Support(String email) {
        this.email = email;
    }
}
