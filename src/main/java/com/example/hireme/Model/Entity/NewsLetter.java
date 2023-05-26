package com.example.hireme.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="news_letters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class NewsLetter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    public NewsLetter(String email) {
        this.email = email;
    }
}
