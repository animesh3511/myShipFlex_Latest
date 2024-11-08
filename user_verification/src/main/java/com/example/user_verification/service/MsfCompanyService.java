package com.example.user_verification.service;

import com.example.user_verification.model.request.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MsfCompanyService {

    Object signUp(MsfCompanyRequest userRequest);

    Object login(String email, String password);

    Object verifyOtpWithLogin(String email, int otp) throws Exception;

    Object findByCompanyId(Long companyId);

    Object saveOrUpdateUser(UserRequest userRequest);

    Object saveOrUpdateStoreDetails(StoreDetailsRequest storeDetailsRequest);

    Object getStoreDetailsByCompanyId();

    Object saveCompanyCarriers(Long msfCompanyCarrierId, List<Long> carrierIds);

    Object getAllMsfCompanyCarriers(Pageable pageable);

    Object saveOrUpdateBrand(BrandRequest brandRequest);

    Object getAllBrandsByCompanyId(Pageable pageable);

    Object saveOrUpdatePickUpLocation(PickUpLocationRequest pickUpLocationRequest);

    Object getAllPickUpLocationByCompanyId(Pageable pageable);

    Object getAllUsersByCompanyId(Pageable pageable);

    Object saveOrUpdatePaymentDetails(PaymentDetailsRequest paymentDetailsRequest);

    Object saveOrUpdateShipmentDetails(ShipmentDetailsRequest shipmentDetailsRequest);

    Object saveOrUpdateProductDetails(ProductDetailsRequest productDetailsRequest);

    Object saveOrUpdateProductDelivery(ProductDeliveryRequest productDeliveryRequest);

    Object saveOrUpdatePersonalShipmentPaymentDetails(PersonalShipmentPaymentDetailsRequest personalShipmentPaymentDetailsRequest);

    Object saveOrUpdateProfileDetails(ProfileDetailsRequest profileDetailsRequest);

    Object saveOrUpdateNewShipmentDetails(NewShipmentDetailsRequest newShipmentDetailsRequest);

    Object getNewShipmentDetailsByCompanyId();

    Object saveOrUpdateCommodityDetails(ListOfCommodityDetailsRequest listOfCommodityDetailsRequest);

    Object saveOrUpdateCommercialInvoice(CommercialInvoiceRequest commercialInvoiceRequest);

    Object saveOrUpdateCargoDetails(CargoDetailsRequest cargoDetailsRequest);

    Object saveOrUpdateNewShipmentMethods(NewShipmentMethodsRequest newShipmentMethodsRequest);

    Object saveOrUpdateInsurance(InsuranceRequest insuranceRequest);

    Object saveOrUpdatePickupScheduler(PickupSchedulerRequest pickupSchedulerRequest);

    Object getNewShipmentDetailsByTrackingId(String trackingId);

    Object getScheduledPickupDetails(String pickupId);

    Object getShipmentByShippingFilter(ShippingFilterRequest shippingFilterRequest);


    //  Object getUserByNameEmailLocation(String name, String email, String location);

   // Object findByLocation(String location);
}
