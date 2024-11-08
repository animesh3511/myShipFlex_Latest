package com.example.user_verification.repository;

import com.example.user_verification.model.DeliveryStatus;
import com.example.user_verification.model.NewShipmentDetails;
import com.example.user_verification.model.ShipmentMethodOptions;
import com.example.user_verification.model.response.FilterResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NewShipmentDetailsRepository extends JpaRepository<NewShipmentDetails, Long> {

    boolean existsByCompanyId(Long companyId);

    List<NewShipmentDetails> findByCompanyId(Long companyId);

    @Query(value = "SELECT MAX(tracking_id) FROM new_msf_shipment_details", nativeQuery = true)
    String findLatestTrackingId();

    NewShipmentDetails findByTrackingId(String trackingId);


   /* @Query("SELECT new com.example.user_verification.model.response.FilterResponse(" +
            "nsd.trackingId, nsd.createdAt, nsd.deliveryStatus, " +
            "nsd.country, nsd.deliveryCountry, nsm.carrierName, nsm.service, " +
            "nsm.carrierPrice, bd.itemName, bd.weight) " +
            "FROM NewShipmentDetails nsd " +
            "JOIN NewShipmentMethods nsm ON nsd.id = nsm.shipmentReferenceId " +
            "JOIN BoxDetails bd ON bd.trackingId = nsd.trackingId " +
            "WHERE (nsm.carrierName = :carrier OR :carrier IS NULL) " +
            "AND (nsd.country = :fromCountry OR :fromCountry IS NULL) " +
            "AND (nsd.pendingReason = :pendingReason OR :pendingReason IS NULL) " +
            "AND (nsm.shipmentMethodOptions = :deliveryMethod OR :deliveryMethod IS NULL) " +
            "AND (nsd.deliveryCountry = :toCountry OR :toCountry IS NULL) " +
            "AND (nsd.deliveryStatus = :trackingStatus OR :trackingStatus IS NULL) " +
            "AND (nsd.createdAt >= :fromDateTime OR :fromDateTime IS NULL) " +
            "AND (nsd.createdAt <= :toDateTime OR :toDateTime IS NULL)")*/

    @Query("SELECT new com.example.user_verification.model.response.FilterResponse(" +
            "nsd.trackingId, nsd.createdAt, nsd.deliveryStatus, " +
            "nsd.country, nsd.deliveryCountry, nsm.carrierName, nsm.service, " +
            "nsm.carrierPrice, bd.itemName, bd.weight) " +
            "FROM NewShipmentDetails nsd " +
            "JOIN NewShipmentMethods nsm ON nsd.id = nsm.shipmentReferenceId " +
            "JOIN BoxDetails bd ON bd.trackingId = nsd.trackingId " +
            "WHERE (nsm.carrierName = :carrier OR :carrier IS NULL) " +
            "AND (nsd.country = :fromCountry OR :fromCountry IS NULL) " +
            "AND (nsd.pendingReason = :pendingReason OR :pendingReason IS NULL) " +
            "AND (nsm.shipmentMethodOptions = :deliveryMethod OR :deliveryMethod IS NULL) " +
            "AND (nsd.deliveryCountry = :toCountry OR :toCountry IS NULL) " +
            "AND (nsd.deliveryStatus = :trackingStatus OR :trackingStatus IS NULL) " +
            "AND (nsd.createdAt >= :fromDateTime OR :fromDateTime IS NULL) " +
            "AND (nsd.createdAt <= :toDateTime OR :toDateTime IS NULL) " +
            "ORDER BY " +
            "CASE " +
            "WHEN :sortBy = 'trackingStatus' THEN nsd.deliveryStatus " +
            "WHEN :sortBy = 'carrier' THEN nsm.carrierName " +
            "WHEN :sortBy = 'service' THEN nsm.service " +
            "WHEN :sortBy = 'fromCountry' THEN nsd.country " +
            "WHEN :sortBy = 'toCountry' THEN nsd.deliveryCountry " +
            "WHEN :sortBy = 'pendingReason' THEN nsd.pendingReason " +
            "WHEN :sortBy = 'fromDate' THEN nsd.createdAt " +
            "WHEN :sortBy = 'toDate' THEN nsd.createdAt " +
            "WHEN :sortBy = 'shippingMethod' THEN nsm.shipmentMethodOptions " +
            "ELSE nsd.trackingId END ")
    List<FilterResponse> findByShippingFilter(@Param("carrier") String carrier,
                                              @Param("fromCountry") String fromCountry,
                                              @Param("fromDateTime") LocalDateTime fromDateTime,
                                              @Param("pendingReason") String pendingReason,
                                              @Param("deliveryMethod") ShipmentMethodOptions deliveryMethod,
                                              @Param("toCountry") String toCountry,
                                              @Param("toDateTime") LocalDateTime toDateTime,
                                              @Param("trackingStatus") DeliveryStatus trackingStatus,
                                              @Param("sortBy") String sortBy);


}
