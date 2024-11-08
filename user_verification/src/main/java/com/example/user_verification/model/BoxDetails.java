package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "box_details")
public class BoxDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "box_details_id")
    private Long boxDetailsId;

    @Column(name = "cargo_details_id")
    private Long cargoDetailsId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "quantity")
    private String quantity;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "weight")
    private String weight;

    @Column(name = "length")
    private String length;

    @Column(name = "width")
    private String width;

    @Column(name = "height")
    private String height;

    @Column(name = "value")
    private String value;



}
