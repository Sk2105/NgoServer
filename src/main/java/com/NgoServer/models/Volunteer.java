package com.NgoServer.models;

import java.util.ArrayList;
import java.util.List;

import com.NgoServer.utils.VolunteerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "volunteers")
public class Volunteer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private VolunteerStatus status = VolunteerStatus.PENDING;

    @ManyToMany
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    public Volunteer(Long id, User user, VolunteerStatus status) {
        this.id = id;
        this.user = user;
        this.status = status;
    }

}