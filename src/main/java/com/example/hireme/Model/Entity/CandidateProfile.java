package com.example.hireme.Model.Entity;

import com.example.hireme.Model.JobType;
import com.example.hireme.Model.Profile;
import com.example.hireme.Model.StudyDegree;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates_profiles")
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfile extends Profile {
    @Enumerated(value = EnumType.STRING)
    private StudyDegree study_degree;

    @Enumerated(value = EnumType.STRING)
    private JobType specialisation;

    private String motivation_letter;
    private String experience;

    public CandidateProfile(String first_name, String last_name, LocalDateTime birth_date, String id_number, Integer mobile_number, StudyDegree study_degree, JobType specialisation, String motivation_letter, String experience) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
        this.study_degree = study_degree;
        this.specialisation = specialisation;
        this.motivation_letter = motivation_letter;
        this.experience = experience;
    }
}
