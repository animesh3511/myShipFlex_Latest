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
@Table(name = "new_shipment_methods")
public class NewShipmentMethods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "new_shipment_method_id")
    private Long newShipmentMethodId;

    @Column(name = "shipment_reference_id")
    private Long shipmentReferenceId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "new_shipment_method_options")
    @Enumerated(EnumType.STRING)
    private ShipmentMethodOptions shipmentMethodOptions;

    @Column(name = "promocode")
    private String promoCode;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "carrier_name")
    private String carrierName;

    @Column(name = "service")
    private String service;

    @Column(name = "carrier_price")
    private String carrierPrice;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
