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
@Table(name = "commercial_invoice")
public class CommercialInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commercial_invoice_id")
    private Long commercialInvoiceId;

    @Column(name = "shipment_reference_id")
    private Long shipmentReferenceId;

    @Column(name = "tacking_id")
    private String trackingId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "invoice_type")
    private String invoiceType;

    @Column(name = "terms_of_sale")
    private String termsOfSale;

    @Column(name = "reason_of_export")
    private String reasonOfExport;

    @Column(name = "country_of_manufacturing")
    private String countryOfMfc;

    @Column(name = "vat_number")
    private String vatNumber;

    @Column(name = "EORI_Number")
    private String eoriNumber;

    @Column(name = "fba_id")
    private String fbaId;

    @Column(name = "amazon_reference_id")
    private String amazonReferenceId;

    @Column(name = "country")
    private String country;

    @Column(name = "name")
    private String name;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "email")
    private String email;

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
