package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import com.example.hireme.Model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private LocalDateTime email_verified_at;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private Boolean active;

    private LocalDateTime created_at;

    @OneToOne(mappedBy = "user" ,cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Profile profile;

    public User(String email, String password, LocalDateTime email_verified_at, Role role, Boolean active, LocalDateTime created_at, Profile profile) {
        this.email = email;
        this.password = password;
        this.email_verified_at = email_verified_at;
        this.role = role;
        this.active = active;
        this.created_at = created_at;
        this.profile = profile;
    }

    public User(String email, String password, LocalDateTime email_verified_at, Role role, Boolean active, LocalDateTime created_at) {
        this.email = email;
        this.password = password;
        this.email_verified_at = email_verified_at;
        this.role = role;
        this.active = active;
        this.created_at = created_at;
    }
}
