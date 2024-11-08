package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "new_msf_shipment_details")
public class NewShipmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_reference_id")
    private Long id;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "country")
    private String country;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_person_name")
    private String contactPersonName;

    @Column(name = "address")
    private String address;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "phone_number")
    private String phoneNo;

    @Column(name = "email")
    private String email;

    @Column(name = "delivery_country")
    private String deliveryCountry;

    @Column(name = "delivery_company_name")
    private String deliveryCompanyName;

    @Column(name = "delivery_contact_person_name")
    private String deliveryContactPersonName;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "delivery_address2")
    private String deliveryAddress2;

    @Column(name = "delivery_city")
    private String deliveryCity;

    @Column(name = "delivery_state")
    private String deliveryState;

    @Column(name = "delivery_zipcode")
    private String deliveryZipcode;

    @Column(name = "delivery_phone_number")
    private String deliveryPhoneNo;

    @Column(name = "delivery_email")
    private String deliveryEmail;

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "pending_reason", columnDefinition = "TEXT")
    private String pendingReason;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;


}
