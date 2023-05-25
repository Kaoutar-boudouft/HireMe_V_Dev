package com.example.hireme.Model.Entity;

import com.example.hireme.Model.Profile;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employers_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class EmployerProfile extends Profile {

}
