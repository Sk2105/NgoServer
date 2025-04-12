package com.NgoServer.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "volunteer_certificates")
public class VolunteerCertificate {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Volunteer volunteer;

    @ManyToOne
    private Certificate certificate;

    @Column(name = "certificate_image_url", nullable = false, unique = true)
    private String certificateImageUrl;

    @Column(name = "certificate_issue_date", nullable = false, unique = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime certificateIssueDate = LocalDateTime.now();

}
