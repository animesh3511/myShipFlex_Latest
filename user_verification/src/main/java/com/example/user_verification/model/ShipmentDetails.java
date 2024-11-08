package com.example.user_verification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.Text;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "shipment_details_")
public class ShipmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_details_id")
    private Long shipmentDetailsId;

    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "d_name")
    private String deliveryName;
    @Column(name = "d_email")
    private String deliveryEmail;
    @Column(name = "d_phone")
    private String deliveryPhone;
    @Column(name = "d_address1")
    private String deliveryAddress1;
    @Column(name = "d_address2")
    private String deliveryAddress2;
    @Column(name = "d_country")
    private String deliveryCountry;
    @Column(name = "d_zipcode")
    private String deliveryZipcode;
    @Column(name = "d_city")
    private String deliveryCity;
    @Column(name = "d_state")
    private String deliveryState;
    @Column(name = "p_name")
    private String pickupName;
    @Column(name = "p_email")
    private String pickupEmail;
    @Column(name = "p_phone")
    private String pickupPhone;
    @Column(name = "p_address1")
    private String pickupAddress1;
    @Column(name = "p_address2")
    private String pickupAddress2;
    @Column(name = "p_country")
    private String pickupCountry;
    @Column(name = "p_zipcode")
    private String pickupZipcode;
    @Column(name = "p_city")
    private String pickupCity;
    @Column(name = "p_state")
    private String pickupState;
    @Column(name = "pending_reason", columnDefinition = "TEXT")
    private String pendingReason;


    @Column(name = "HSN_code")
    private String hsnCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "length")
    private String length;
    @Column(name = "width")
    private String width;
    @Column(name = "height")
    private String height;
    @Column(name = "item_volumetric_weight")
    private String itemVolumetricWeight;
    @Column(name = "item_physical_weight")
    private String itemPhysicalWeight;


    @Column(name = "delivery_type")
    private String deliveryType;
    @Column(name = "carrier_id")
    private Long carrierId;
    @Column(name = "carrier_name")
    private String carrierName;
    @Column(name = "carrier_markup_percentage")
    private String carrierMarkupPercentage;
    @Column(name = "delivery_document")
    private String deliveryDocument;


    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "cvv")
    private String cvv;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "mm_yy")
    private String monthYear;


    @Column(name = "profile_first_name")
    private String profileFirstName;
    @Column(name = "profile_last_name")
    private String profileLastName;
    @Column(name = "email_id")
    private String emailId;
    @Column(name = "phoneNo")
    private String phoneNo;
    @Column(name = "address")
    private String address;
    @Column(name = "address_1")
    private String address1;
    @Column(name = "postal_code")
    private String postalCode;
    @Column(name = "location")
    private String location;

}
