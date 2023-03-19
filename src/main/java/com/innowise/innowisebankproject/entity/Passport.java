package com.innowise.innowisebankproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "passport")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Passport {
    @Id
    @Column(name = "passport_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "firstname")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "succession")
    private String succession;

    @Column(name = "identity_code")
    private String identityCode;
}
