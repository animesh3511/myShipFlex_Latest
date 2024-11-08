package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "charge_details")
@Table(name = "charge_details")
public class ChargeDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "name")
    private String name;

    @Column(name = "charge_id")
    private String chargeId;

    @Column(name = "email")
    private String email;

    @Column(name = "last4")
    private String last4;

    @Column(name = "card")
    private String card;


}
