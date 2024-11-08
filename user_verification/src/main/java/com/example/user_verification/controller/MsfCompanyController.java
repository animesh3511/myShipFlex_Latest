package com.example.user_verification.controller;

import com.example.user_verification.model.request.*;
import com.example.user_verification.model.response.CustomEntityResponse;
import com.example.user_verification.model.response.EntityResponse;
import com.example.user_verification.service.MsfCompanyService;
import com.example.user_verification.serviceimpl.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class MsfCompanyController {

    @Autowired
    private MsfCompanyService msfCompanyService;
    @Autowired
    private UserToken userToken;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MsfCompanyRequest msfCompanyRequest) {

        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.signUp(msfCompanyRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.login(loginRequest.getEmail(), loginRequest.getPassword()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verifyOtpWithLogin")
    public ResponseEntity<?> verifyOtpWithLogin(@RequestParam String email, @RequestParam int otp) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.verifyOtpWithLogin(email, otp), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findByCompanyId")
    public ResponseEntity<?> findByCompanyId() {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.findByCompanyId
                    (userToken.getCompanyFromToken().getCompanyId()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/saveOrUpdateUser")
    public ResponseEntity<?> saveOrUpdateUser(@RequestBody UserRequest userRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateUser(userRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllUsersByCompanyId")
    public ResponseEntity<?> getAllUsersByCompanyId(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                    @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getAllUsersByCompanyId(pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateStoreDetails")
    public ResponseEntity<?> saveOrUpdateStoreDetails(@RequestBody StoreDetailsRequest storeDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateStoreDetails(storeDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getStoreDetailsByCompanyId")
    public ResponseEntity<?> getStoreDetailsByCompanyId() {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getStoreDetailsByCompanyId(), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveCompanyCarriers")
    public ResponseEntity<?> saveCompanyCarriers(@RequestBody MsfCompanyCarriersRequest msfCompanyCarriersRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveCompanyCarriers(msfCompanyCarriersRequest.getMsfCompanyCarrierId(),
                    msfCompanyCarriersRequest.getCarrierIds()), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllMsfCompanyCarriers")
    public ResponseEntity<?> getAllMsfCompanyCarriers(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                      @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getAllMsfCompanyCarriers(pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/saveOrUpdateBrand")
    public ResponseEntity<?> saveOrUpdateBrand(@ModelAttribute BrandRequest brandRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateBrand(brandRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAllBrandsByCompanyId")
    public ResponseEntity<?> getAllBrandsByCompanyId(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                     @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getAllBrandsByCompanyId(pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdatePickUpLocation")
    public ResponseEntity<?> saveOrUpdatePickUpLocation(@ModelAttribute PickUpLocationRequest pickUpLocationRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdatePickUpLocation(pickUpLocationRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getAllPickUpLocationByCompanyId")
    public ResponseEntity<?> getAllPickUpLocationByCompanyId(@RequestParam(defaultValue = "0", required = false) Integer pageNo,
                                                             @RequestParam(defaultValue = "50", required = false) Integer pageSize) {
        Pageable pageable = PageRequest.of(Optional.ofNullable(pageNo).orElse(0),
                Optional.ofNullable(pageSize).orElse(50));
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getAllPickUpLocationByCompanyId(pageable), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdatePaymentDetails")
    public ResponseEntity<?> saveOrUpdatePaymentDetails(@RequestBody PaymentDetailsRequest paymentDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdatePaymentDetails(paymentDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateShipmentDetails")
    public ResponseEntity<?> saveOrUpdateShipmentDetails(@RequestBody ShipmentDetailsRequest shipmentDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateShipmentDetails(shipmentDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/saveOrUpdateProductDetails")
    public ResponseEntity<?> saveOrUpdateProductDetails(@RequestBody ProductDetailsRequest productDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateProductDetails(productDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateProductDelivery")
    public ResponseEntity<?> saveOrUpdateProductDelivery(@ModelAttribute ProductDeliveryRequest productDeliveryRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateProductDelivery(productDeliveryRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdatePersonalShipmentPaymentDetails")
    public ResponseEntity<?> saveOrUpdatePersonalShipmentPaymentDetails(@RequestBody PersonalShipmentPaymentDetailsRequest personalShipmentPaymentDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdatePersonalShipmentPaymentDetails(personalShipmentPaymentDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateProfileDetails")
    public ResponseEntity<?> saveOrUpdateProfileDetails(@RequestBody ProfileDetailsRequest profileDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateProfileDetails(profileDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateNewShipmentDetails")
    public ResponseEntity<?> saveOrUpdateNewShipmentDetails(@RequestBody NewShipmentDetailsRequest newShipmentDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateNewShipmentDetails(newShipmentDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getNewShipmentDetailsByCompanyId")
    public ResponseEntity<?> getNewShipmentDetailsByCompanyId() {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getNewShipmentDetailsByCompanyId(), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/saveOrUpdateCommodityDetails")
    public ResponseEntity<?> saveOrUpdateCommodityDetails(@RequestBody ListOfCommodityDetailsRequest listOfCommodityDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateCommodityDetails(listOfCommodityDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateCommercialInvoice")
    public ResponseEntity<?> saveOrUpdateCommercialInvoice(@RequestBody CommercialInvoiceRequest commercialInvoiceRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateCommercialInvoice(commercialInvoiceRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateCargoDetails")
    public ResponseEntity<?> saveOrUpdateCargoDetails(@RequestBody CargoDetailsRequest cargoDetailsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateCargoDetails(cargoDetailsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateNewShipmentMethods")
    public ResponseEntity<?> saveOrUpdateNewShipmentMethods(@RequestBody NewShipmentMethodsRequest newShipmentMethodsRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateNewShipmentMethods(newShipmentMethodsRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdateInsurance")
    public ResponseEntity<?> saveOrUpdateInsurance(@RequestBody InsuranceRequest insuranceRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdateInsurance(insuranceRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveOrUpdatePickupScheduler")
    public ResponseEntity<?> saveOrUpdatePickupScheduler(@RequestBody PickupSchedulerRequest pickupSchedulerRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.saveOrUpdatePickupScheduler(pickupSchedulerRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getNewShipmentDetailsByTrackingId")
    public ResponseEntity<?> getNewShipmentDetailsByTrackingId(@RequestParam String trackingId) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getNewShipmentDetailsByTrackingId(trackingId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getScheduledPickupDetails")
    public ResponseEntity<?> getScheduledPickupDetails(@RequestParam String pickupId) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getScheduledPickupDetails(pickupId), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getShipmentByShippingFilter")
    public ResponseEntity<?> getShipmentByShippingFilter(@RequestBody ShippingFilterRequest shippingFilterRequest) {
        try {
            return new ResponseEntity<>(new EntityResponse(msfCompanyService.getShipmentByShippingFilter(shippingFilterRequest), 0), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new CustomEntityResponse(e.getMessage(), -1), HttpStatus.BAD_REQUEST);
        }
    }

    //class ends here
}
