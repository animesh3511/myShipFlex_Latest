package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommercialInvoiceRequest {

    private Long commercialInvoiceId;
    private Long shipmentReferenceId;
    private String trackingId;
    private String invoiceType;
    private String termsOfSale;
    private String reasonOfExport;
    private String countryOfMfc;
    private String vatNumber;
    private String eoriNumber;
    private String fbaId;
    private String amazonReferenceId;
    private String country;
    private String name;
    private String taxId;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String postCode;
    private String email;
}




