package com.NgoServer.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "donors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double totalDonation = 0.0;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastDonation = LocalDateTime.now();


    @OneToMany(mappedBy = "donor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Donation> donations = new ArrayList<>();


    public Donor(Long id, User user, Double totalDonation, LocalDateTime lastDonation) {
        this.id = id;
        this.user = user;
        this.totalDonation = totalDonation;
        this.lastDonation = lastDonation;
    }

}
