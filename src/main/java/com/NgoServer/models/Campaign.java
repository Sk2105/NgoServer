package com.NgoServer.models;
import java.time.LocalDateTime;

import com.NgoServer.utils.CampaignStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "campaigns")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String title;
    @Column(nullable = false,length = 5000)
    private String description;
    @Column(nullable = false)
    private Double goalAmount;
    private LocalDateTime createdAt;
    private Double collectedAmount;
    @Enumerated(EnumType.STRING)
    private CampaignStatus status; // ACTIVE, COMPLETED, CANCELLED
}
