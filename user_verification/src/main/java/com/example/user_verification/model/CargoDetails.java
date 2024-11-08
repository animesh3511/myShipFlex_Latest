package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "cargo_details")
public class CargoDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_details_id")
    private Long cargoDetailsId;

    @Column(name = "shipment_reference_id")
    private Long shipmentReferenceId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "company_id")
    private Long companyId;

    @Enumerated(EnumType.STRING)
    private PackageType packageType;

    @Column(name = "cargo_details_file")
    private String cargoDetailsFile;

    @Column(name = "note")
    private String note;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
