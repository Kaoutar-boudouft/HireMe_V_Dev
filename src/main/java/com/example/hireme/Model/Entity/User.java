package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import com.example.hireme.Model.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users" ,  uniqueConstraints = @UniqueConstraint (columnNames = "email"))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User implements UserDetails {
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

    @OneToOne(mappedBy = "user")
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.profile.getFirst_name()+" "+this.profile.getLast_name();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.email_verified_at!=null;
    }
}
