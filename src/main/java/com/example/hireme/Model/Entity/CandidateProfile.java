package com.example.hireme.Model.Entity;

import com.example.hireme.Model.JobType;
import com.example.hireme.Model.Profile;
import com.example.hireme.Model.StudyDegree;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "candidates_profiles")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Setter
@Getter
public class CandidateProfile extends Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StudyDegree study_degree;

    @Enumerated(value = EnumType.STRING)
    private JobType specialisation;

    private String motivation_letter;
    private String experience;

    @ManyToMany
    @JoinTable(name = "candidatures", joinColumns = @JoinColumn(name = "candidate_profile_id"), inverseJoinColumns = @JoinColumn(name = "offer_id"))
    List<JobOffer> job_offers;

    public CandidateProfile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, User user, City city, StudyDegree study_degree, JobType specialisation, String motivation_letter, String experience, List<JobOffer> job_offers) {
        super(first_name, last_name, birth_date, id_number, mobile_number, user, city);
        this.study_degree = study_degree;
        this.specialisation = specialisation;
        this.motivation_letter = motivation_letter;
        this.experience = experience;
        this.job_offers = job_offers;
    }

    public CandidateProfile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, StudyDegree study_degree, JobType specialisation, String motivation_letter, String experience, List<JobOffer> job_offers) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
        this.study_degree = study_degree;
        this.specialisation = specialisation;
        this.motivation_letter = motivation_letter;
        this.experience = experience;
        this.job_offers = job_offers;
    }

    public CandidateProfile(String first_name, String last_name, LocalDate birth_date, String id_number, Integer mobile_number, StudyDegree study_degree, JobType specialisation, String motivation_letter, String experience) {
        super(first_name, last_name, birth_date, id_number, mobile_number);
        this.study_degree = study_degree;
        this.specialisation = specialisation;
        this.motivation_letter = motivation_letter;
        this.experience = experience;
    }

    public CandidateProfile(String first_name, String last_name, LocalDate birth_date) {
        super(first_name, last_name, birth_date);
    }
}
