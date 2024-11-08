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
@Table(name = "commodity_details")
public class CommodityDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commodity_details_id")
    private Long commodityDetailsId;

    @Column(name = "shipment_reference_id")
    private Long shipmentReferenceId;

    @Column(name = "tracking_id")
    private String trackingId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "units")
    private String units;

    @Column(name = "HS_Code")
    private String hsCode;

    @Column(name = "unit_price")
    private String unitPrice;

    @Column(name = "unit_weight")
    private String unitWeight;

    @Column(name = "of_boxes_contain_it")
    private String ofBoxesContainIt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updatedAt;


}
