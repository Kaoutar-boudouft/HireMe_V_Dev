package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employers_profiles")
@NoArgsConstructor
public class EmployerProfile extends Profile {

    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
