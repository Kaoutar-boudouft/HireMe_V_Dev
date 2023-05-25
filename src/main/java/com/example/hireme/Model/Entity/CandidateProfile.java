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
}
