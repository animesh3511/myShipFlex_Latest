package com.example.user_verification.serviceimpl;


import com.example.user_verification.model.*;
import com.example.user_verification.model.request.*;
import com.example.user_verification.model.response.FilterResponse;
import com.example.user_verification.model.response.ScheduledPickupResponse;
import com.example.user_verification.model.response.TrackingDetailsResponse;
import com.example.user_verification.repository.*;
import com.example.user_verification.service.MsfCompanyService;
import com.example.user_verification.service.TokenService;
import com.example.user_verification.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MsfCompanyServiceImpl implements MsfCompanyService {

    @Autowired
    private MsfCompanyRepository msfCompanyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserToken userToken;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private StoreDetailsRepository storeDetailsRepository;
    @Autowired
    private PickupSchedulerRepository pickupSchedulerRepository;
    @Autowired
    private CargoDetailsRepository cargoDetailsRepository;
    @Autowired
    private BoxDetailsRepository boxDetailsRepository;
    @Autowired
    private CommercialInvoiceRepository commercialInvoiceRepository;
    @Autowired
    private CommodityDetailsRepository commodityDetailsRepository;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private NewShipmentMethodsRepository newShipmentMethodsRepository;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private PickUpLocationRepository pickUpLocationRepository;
    @Autowired
    private ShipmentDetailsRepository shipmentDetailsRepository;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private MsfCompanyCarriersRepository msfCompanyCarriersRepository;
    @Autowired
    private NewShipmentDetailsRepository newShipmentDetailsRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public Object signUp(MsfCompanyRequest msfCompanyRequest) {

        MsfCompany msfCompany = new MsfCompany();
        msfCompany.setName(msfCompanyRequest.getName());
        if (msfCompanyRepository.existsByEmail(msfCompanyRequest.getEmail())) {
            return "Email already exists";
        } else {
            msfCompany.setEmail(msfCompanyRequest.getEmail());
        }
        msfCompany.setLocation(msfCompanyRequest.getLocation());
        if (isMobNoValid(msfCompanyRequest.getMobNumber())) {
            if (msfCompanyRepository.existsByMobNumber(msfCompanyRequest.getMobNumber())) {
                return "Mobile number already exists";
            } else {
                msfCompany.setMobNumber(msfCompanyRequest.getMobNumber());
            }
        } else {
            return "Mobile Number must be of exactly 10 digits";
        }
        //   msfCompany.setMobNumber(msfCompanyRequest.getMobNumber());
        msfCompany.setPassword(hashPassword(msfCompanyRequest.getPassword()));
        msfCompany.setIsActive(true);
        msfCompany.setIsDeleted(false);
        MsfCompany msfCompany1 = msfCompanyRepository.save(msfCompany);
        msfCompany.setCompanyId(msfCompany1.getId());
        msfCompanyRepository.save(msfCompany);
        sendCredentialEmail(msfCompanyRequest.getEmail(), msfCompanyRequest.getPassword());
        return "Please check your Email";
    }

    @Override
    public Object login(String email, String password) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        MsfCompany msfCompany = userDetails.getMsfCompany();
        System.out.println("Email after authentication: " + msfCompany.getEmail());
        System.out.println("Is Authenticated: " + authenticate.isAuthenticated());

        if (msfCompanyRepository.existsByEmail(email)) {
            MsfCompany msfCompany1 = msfCompanyRepository.findByEmail(email).get();
            if (passwordEncoder.matches(password, msfCompany1.getPassword())) {
                sendEmailWithOTP(msfCompany1.getEmail());
                return "Login succesfull";
            } else {
                return "Incorrect Password";
            }
        } else {
            return "Email does not exists";
        }
    }

   /* String token;
    public String getToken() {
        return token;
    }*/

    @Override
    public Object verifyOtpWithLogin(String email, int otp) throws Exception {

        if (msfCompanyRepository.existsByEmail(email)) {
            int cacheOtp = otpService.getOtp(email);
            //MsfCompany msfCompany = msfCompanyRepository.findByEmail(email).get();
            if (cacheOtp == otp) {
                otpService.clearOTP(email);
                //return "OTP verification succesfull JWT Token "+jwtTokenUtil.generateToken(email);
                String token = jwtTokenUtil.generateToken(email);
                //Long companyId = userToken.getCompanyFromToken().getCompanyId();
                //tokenService.saveToken(companyId, token);
                return ResponseEntity.ok()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .body("OTP verification succesfull");
            } else {
                return "OTP verification failed due to incorrect otp";
            }
        } else {
            return "Email does not exist";
        }
    }

    @Override
    public Object findByCompanyId(Long companyId) {
        if (msfCompanyRepository.existsByCompanyId(companyId)) {
            MsfCompany msfCompany = msfCompanyRepository.findByCompanyId(companyId);
            if (!msfCompany.getIsDeleted()) {
                return msfCompany;
            } else {
                return "Company details are deleted";
            }
        } else {
            return "Company not found";
        }
    }

    @Override
    public Object saveOrUpdateUser(UserRequest userRequest) {

        if (userRepository.existsById(userRequest.getUserId())) {
            User user = userRepository.findById(userRequest.getUserId()).get();
            user.setUserId(userRequest.getUserId());
            user.setIndustry(userRequest.getIndustry());
            user.setLastName(userRequest.getLastName());
            user.setFirstName(userRequest.getFirstName());
            user.setCompanyName(userRequest.getCompanyName());
            user.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            user.setLocation(userRequest.getLocation());
            List<Long> userId = new ArrayList<>();
            userId.add(userRequest.getUserId());
            if (userRepository.existsByPhoneNumberAndUserIdNotIn(userRequest.getPhoneNumber(), userId)) {
                return "Phone Number already exists";
            } else {
                user.setPhoneNumber(userRequest.getPhoneNumber());
            }
            userRepository.save(user);
            return "User details are updated";
        } else {
            if (msfCompanyRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
                User user = new User();
                user.setLocation(userRequest.getLocation());
                user.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
                user.setCompanyName(userRequest.getCompanyName());
                user.setFirstName(userRequest.getFirstName());
                user.setLastName(userRequest.getLastName());
                user.setIndustry(userRequest.getIndustry());
                if (isMobNoValid(userRequest.getPhoneNumber())) {
                    if (userRepository.existsByPhoneNumber(userRequest.getPhoneNumber())) {
                        return "Phone number already exists";
                    } else {
                        user.setPhoneNumber(userRequest.getPhoneNumber());
                    }
                } else {
                    return "Phone number must contain exactly 10 digits";
                }
                userRepository.save(user);
            }
            return "User Details are saved";
        }
    }


    @Override
    public Object getAllUsersByCompanyId(Pageable pageable) {

        if (userRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            return userRepository.findAllByCompanyId(userToken.getCompanyFromToken().getCompanyId(), pageable);
        } else {
            return "Not found";
        }

    }

    @Override
    public Object saveOrUpdatePaymentDetails(PaymentDetailsRequest paymentDetailsRequest) {

        if (paymentDetailsRepository.existsById(paymentDetailsRequest.getPaymentDetailsId())) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findByPaymentDetailsId(paymentDetailsRequest.getPaymentDetailsId());
            paymentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdatePaymentDetails1(paymentDetails, paymentDetailsRequest);
            return "Updated";
        } else {
            PaymentDetails paymentDetails = new PaymentDetails();
            paymentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdatePaymentDetails1(paymentDetails, paymentDetailsRequest);
            return "Created";
        }
    }

    private void saveOrUpdatePaymentDetails1(PaymentDetails paymentDetails, PaymentDetailsRequest paymentDetailsRequest) {

        paymentDetails.setCardNumber(paymentDetailsRequest.getCardNumber());
        paymentDetails.setCardType(paymentDetailsRequest.getCardType());
        paymentDetails.setCvv(paymentDetailsRequest.getCvv());
        paymentDetails.setFirstName(paymentDetailsRequest.getFirstName());
        paymentDetails.setLastName(paymentDetailsRequest.getLastName());
        paymentDetails.setMonthYear(paymentDetailsRequest.getMonthYear());
        paymentDetails.setPaymentDetailsId(paymentDetailsRequest.getPaymentDetailsId());
        paymentDetails.setPaymentMethod(paymentDetailsRequest.getPaymentMethod());
        paymentDetailsRepository.save(paymentDetails);
    }


    @Override
    public Object saveOrUpdateShipmentDetails(ShipmentDetailsRequest shipmentDetailsRequest) {

        if (shipmentDetailsRepository.existsById(shipmentDetailsRequest.getShipmentDetailsId())) {
            ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByShipmentDetailsId(shipmentDetailsRequest.getShipmentDetailsId());
            shipmentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateShipmentDetails1(shipmentDetails, shipmentDetailsRequest);
            return "Updated";
        } else {
            ShipmentDetails shipmentDetails = new ShipmentDetails();
            shipmentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateShipmentDetails1(shipmentDetails, shipmentDetailsRequest);
            return "Created";
        }

    }

    private void saveOrUpdateShipmentDetails1(ShipmentDetails shipmentDetails, ShipmentDetailsRequest shipmentDetailsRequest) {

        shipmentDetails.setShipmentDetailsId(shipmentDetailsRequest.getShipmentDetailsId());
        shipmentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
        shipmentDetails.setShipmentType(shipmentDetailsRequest.getShipmentType());
        shipmentDetails.setPickupName(shipmentDetailsRequest.getPickupName());
        shipmentDetails.setPickupEmail(shipmentDetailsRequest.getPickupEmail());
        shipmentDetails.setPickupPhone(shipmentDetailsRequest.getPickupPhone());
        shipmentDetails.setPickupAddress1(shipmentDetailsRequest.getPickupAddress1());
        shipmentDetails.setPickupAddress2(shipmentDetailsRequest.getPickupAddress2());
        shipmentDetails.setPickupZipcode(shipmentDetailsRequest.getPickupZipcode());
        shipmentDetails.setPickupCity(shipmentDetailsRequest.getPickupCity());
        shipmentDetails.setPickupState(shipmentDetailsRequest.getPickupState());
        shipmentDetails.setPickupCountry(shipmentDetailsRequest.getPickupCountry());
        shipmentDetails.setDeliveryName(shipmentDetailsRequest.getDeliveryName());
        shipmentDetails.setDeliveryEmail(shipmentDetailsRequest.getDeliveryEmail());
        shipmentDetails.setDeliveryPhone(shipmentDetailsRequest.getDeliveryPhone());
        shipmentDetails.setDeliveryAddress1(shipmentDetailsRequest.getDeliveryAddress1());
        shipmentDetails.setDeliveryAddress2(shipmentDetailsRequest.getDeliveryAddress2());
        shipmentDetails.setDeliveryZipcode(shipmentDetailsRequest.getDeliveryZipcode());
        shipmentDetails.setDeliveryCity(shipmentDetailsRequest.getDeliveryCity());
        shipmentDetails.setDeliveryState(shipmentDetailsRequest.getDeliveryState());
        shipmentDetails.setDeliveryCountry(shipmentDetailsRequest.getDeliveryCountry());
        shipmentDetails.setPendingReason(shipmentDetailsRequest.getPendingReason());
        shipmentDetailsRepository.save(shipmentDetails);

    }

    @Override
    public Object saveOrUpdateProductDetails(ProductDetailsRequest productDetailsRequest) {

        if (shipmentDetailsRepository.existsByShipmentDetailsId(productDetailsRequest.getShipmentDetailsId())) {
            ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByShipmentDetailsId(productDetailsRequest.getShipmentDetailsId());
            shipmentDetails.setHsnCode(productDetailsRequest.getHsnCode());
            shipmentDetails.setItemName(productDetailsRequest.getItemName());
            shipmentDetails.setLength(productDetailsRequest.getLength());
            shipmentDetails.setWidth(productDetailsRequest.getWidth());
            shipmentDetails.setHeight(productDetailsRequest.getHeight());
            shipmentDetails.setItemVolumetricWeight(productDetailsRequest.getItemVolumetricWeight());
            shipmentDetails.setItemPhysicalWeight(productDetailsRequest.getItemPhysicalWeight());
            shipmentDetailsRepository.save(shipmentDetails);
        }
        return "product details saved";
    }

    @Override
    public Object saveOrUpdateProductDelivery(ProductDeliveryRequest productDeliveryRequest) {

        if (shipmentDetailsRepository.existsByShipmentDetailsId(productDeliveryRequest.getShipmentDetailsId())) {

            ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByShipmentDetailsId(productDeliveryRequest.getShipmentDetailsId());
            shipmentDetails.setDeliveryType(productDeliveryRequest.getDeliveryType());
            shipmentDetails.setCarrierId(productDeliveryRequest.getCarrierId());
            shipmentDetails.setCarrierMarkupPercentage(productDeliveryRequest.getCarrierMarkupPercentage());
            shipmentDetails.setCarrierName(productDeliveryRequest.getCarrierName());
            if (productDeliveryRequest.getDeliveryDocument() != null) {
                shipmentDetails.setDeliveryDocument(imageUploadService.uploadImage(productDeliveryRequest.getDeliveryDocument()));
            }
            shipmentDetailsRepository.save(shipmentDetails);
        }
        return "product delivery saved";
    }

    @Override
    public Object saveOrUpdatePersonalShipmentPaymentDetails(PersonalShipmentPaymentDetailsRequest personalShipmentPaymentDetailsRequest) {

        if (shipmentDetailsRepository.existsByShipmentDetailsId(personalShipmentPaymentDetailsRequest.getShipmentDetailsId())) {
            ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByShipmentDetailsId(personalShipmentPaymentDetailsRequest.getShipmentDetailsId());
            shipmentDetails.setPaymentMethod(personalShipmentPaymentDetailsRequest.getPaymentMethod());
            shipmentDetails.setFirstName(personalShipmentPaymentDetailsRequest.getFirstName());
            shipmentDetails.setLastName(personalShipmentPaymentDetailsRequest.getLastName());
            shipmentDetails.setCardNumber(personalShipmentPaymentDetailsRequest.getCardNumber());
            shipmentDetails.setCvv(personalShipmentPaymentDetailsRequest.getCvv());
            shipmentDetails.setCardType(personalShipmentPaymentDetailsRequest.getCardType());
            shipmentDetails.setMonthYear(personalShipmentPaymentDetailsRequest.getMonthYear());
            shipmentDetailsRepository.save(shipmentDetails);
        }
        return "personal payment details saved";
    }

    @Override
    public Object saveOrUpdateProfileDetails(ProfileDetailsRequest profileDetailsRequest) {

        if (shipmentDetailsRepository.existsByShipmentDetailsId(profileDetailsRequest.getShipmentDetailsId())) {
            ShipmentDetails shipmentDetails = shipmentDetailsRepository.findByShipmentDetailsId(profileDetailsRequest.getShipmentDetailsId());
            shipmentDetails.setProfileFirstName(profileDetailsRequest.getProfileFirstName());
            shipmentDetails.setProfileLastName(profileDetailsRequest.getProfileLastName());
            shipmentDetails.setEmailId(profileDetailsRequest.getEmailId());
            shipmentDetails.setPhoneNo(profileDetailsRequest.getPhoneNo());
            shipmentDetails.setAddress(profileDetailsRequest.getAddress());
            shipmentDetails.setAddress1(profileDetailsRequest.getAddress1());
            shipmentDetails.setPickupAddress1(profileDetailsRequest.getAddress1());
            shipmentDetails.setPostalCode(profileDetailsRequest.getPostalCode());
            shipmentDetails.setLocation(profileDetailsRequest.getLocation());
            shipmentDetailsRepository.save(shipmentDetails);
        }
        return "profile details are saved";
    }

    @Override
    public Object saveOrUpdateNewShipmentDetails(NewShipmentDetailsRequest newShipmentDetailsRequest) {

        if (newShipmentDetailsRepository.existsById(newShipmentDetailsRequest.getId())) {
            NewShipmentDetails newShipmentDetails = newShipmentDetailsRepository.findById(newShipmentDetailsRequest.getId()).get();
            newShipmentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateNewShipmentDetails1(newShipmentDetails, newShipmentDetailsRequest);
            return "Updated";
        } else {
            NewShipmentDetails newShipmentDetails = new NewShipmentDetails();
            newShipmentDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            newShipmentDetails.setIsActive(true);
            newShipmentDetails.setIsDeleted(false);
            newShipmentDetails.setTrackingId(generateTrackingId());
            saveOrUpdateNewShipmentDetails1(newShipmentDetails, newShipmentDetailsRequest);
            return "Created";
        }
    }

    private void saveOrUpdateNewShipmentDetails1(NewShipmentDetails newShipmentDetails, NewShipmentDetailsRequest newShipmentDetailsRequest) {

        newShipmentDetails.setCompanyName(newShipmentDetailsRequest.getCompanyName());
        newShipmentDetails.setCountry(newShipmentDetailsRequest.getCountry());
        newShipmentDetails.setCity(newShipmentDetailsRequest.getCity());
        newShipmentDetails.setAddress(newShipmentDetailsRequest.getAddress());
        newShipmentDetails.setAddress2(newShipmentDetailsRequest.getAddress2());
        newShipmentDetails.setContactPersonName(newShipmentDetailsRequest.getContactPersonName());
        newShipmentDetails.setDeliveryAddress(newShipmentDetailsRequest.getDeliveryAddress());
        newShipmentDetails.setDeliveryAddress2(newShipmentDetailsRequest.getDeliveryAddress2());
        newShipmentDetails.setDeliveryCity(newShipmentDetailsRequest.getDeliveryCity());
        newShipmentDetails.setDeliveryCompanyName(newShipmentDetailsRequest.getDeliveryCompanyName());
        newShipmentDetails.setDeliveryContactPersonName(newShipmentDetailsRequest.getDeliveryContactPersonName());
        newShipmentDetails.setDeliveryCountry(newShipmentDetailsRequest.getDeliveryCountry());
        newShipmentDetails.setDeliveryEmail(newShipmentDetailsRequest.getDeliveryEmail());
        newShipmentDetails.setDeliveryPhoneNo(newShipmentDetailsRequest.getDeliveryPhoneNo());
        newShipmentDetails.setDeliveryState(newShipmentDetailsRequest.getDeliveryState());
        newShipmentDetails.setDeliveryZipcode(newShipmentDetailsRequest.getDeliveryZipcode());
        newShipmentDetails.setEmail(newShipmentDetailsRequest.getEmail());
        newShipmentDetails.setPhoneNo(newShipmentDetailsRequest.getPhoneNo());
        newShipmentDetails.setState(newShipmentDetailsRequest.getState());
        newShipmentDetails.setZipcode(newShipmentDetailsRequest.getZipcode());
        newShipmentDetails.setDeliveryStatus(newShipmentDetailsRequest.getDeliveryStatus());
        newShipmentDetails.setPendingReason(newShipmentDetailsRequest.getPendingReason());
        newShipmentDetailsRepository.save(newShipmentDetails);

    }

    @Override
    public Object getNewShipmentDetailsByCompanyId() {
        if (newShipmentDetailsRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            return newShipmentDetailsRepository.findByCompanyId(userToken.getCompanyFromToken().getCompanyId());
        } else {
            return "not found";
        }
    }

    @Override
    public Object saveOrUpdateCommodityDetails(ListOfCommodityDetailsRequest listOfCommodityDetailsRequest) {
        List<String> results = new ArrayList<>();

        for (CommodityDetailsRequest cdr : listOfCommodityDetailsRequest.getCommodityDetailsRequestList()) {
            if (commodityDetailsRepository.existsByCommodityDetailsId(cdr.getCommodityDetailsId())) {
                CommodityDetails commodityDetails = commodityDetailsRepository.findById(cdr.getCommodityDetailsId()).get();
                commodityDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
                saveOrUpdateCommodityDetails1(commodityDetails, cdr);
                results.add("updated");
            } else {
                CommodityDetails commodityDetails = new CommodityDetails();
                commodityDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
                commodityDetails.setIsActive(true);
                commodityDetails.setIsDeleted(false);
                commodityDetails.setTrackingId(cdr.getTrackingId());
                saveOrUpdateCommodityDetails1(commodityDetails, cdr);
                results.add("created");
            }
        }
        return results;
    }

    private void saveOrUpdateCommodityDetails1(CommodityDetails commodityDetails, CommodityDetailsRequest cdr) {
        commodityDetails.setHsCode(cdr.getHsCode());
        commodityDetails.setItemName(cdr.getItemName());
        commodityDetails.setShipmentReferenceId(cdr.getShipmentReferenceId());
        commodityDetails.setOfBoxesContainIt(cdr.getOfBoxesContainIt());
        commodityDetails.setUnitPrice(cdr.getUnitPrice());
        commodityDetails.setUnits(cdr.getUnits());
        commodityDetails.setUnitWeight(cdr.getUnitWeight());
        commodityDetailsRepository.save(commodityDetails); // Save the commodity details
    }

    @Override
    public Object saveOrUpdateCommercialInvoice(CommercialInvoiceRequest commercialInvoiceRequest) {

        if (commercialInvoiceRepository.existsByCommercialInvoiceId(commercialInvoiceRequest.getCommercialInvoiceId())) {
            CommercialInvoice commercialInvoice = commercialInvoiceRepository.findById(commercialInvoiceRequest.getCommercialInvoiceId()).get();
            commercialInvoice.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateCommercialInvoiceDetails(commercialInvoice, commercialInvoiceRequest);
            return "Updated";
        } else {
            CommercialInvoice commercialInvoice = new CommercialInvoice();
            commercialInvoice.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            commercialInvoice.setIsActive(true);
            commercialInvoice.setIsDeleted(false);
            commercialInvoice.setTrackingId(commercialInvoiceRequest.getTrackingId());
            saveOrUpdateCommercialInvoiceDetails(commercialInvoice, commercialInvoiceRequest);
            return "Created";
        }
    }

    private void saveOrUpdateCommercialInvoiceDetails(CommercialInvoice commercialInvoice, CommercialInvoiceRequest commercialInvoiceRequest) {

        commercialInvoice.setAmazonReferenceId(commercialInvoiceRequest.getAmazonReferenceId());
        commercialInvoice.setCountryOfMfc(commercialInvoiceRequest.getCountryOfMfc());
        commercialInvoice.setEoriNumber(commercialInvoiceRequest.getEoriNumber());
        commercialInvoice.setFbaId(commercialInvoiceRequest.getFbaId());
        commercialInvoice.setInvoiceType(commercialInvoiceRequest.getInvoiceType());
        commercialInvoice.setReasonOfExport(commercialInvoiceRequest.getReasonOfExport());
        commercialInvoice.setTermsOfSale(commercialInvoiceRequest.getTermsOfSale());
        commercialInvoice.setVatNumber(commercialInvoiceRequest.getVatNumber());
        commercialInvoice.setCountry(commercialInvoiceRequest.getCountry());
        commercialInvoice.setAddress(commercialInvoiceRequest.getAddress());
        commercialInvoice.setTaxId(commercialInvoiceRequest.getTaxId());
        commercialInvoice.setPostCode(commercialInvoiceRequest.getPostCode());
        commercialInvoice.setPhone(commercialInvoiceRequest.getPhone());
        commercialInvoice.setState(commercialInvoiceRequest.getState());
        commercialInvoice.setName(commercialInvoiceRequest.getName());
        commercialInvoice.setEmail(commercialInvoiceRequest.getEmail());
        commercialInvoice.setCity(commercialInvoiceRequest.getCity());
        commercialInvoice.setShipmentReferenceId(commercialInvoiceRequest.getShipmentReferenceId());
        commercialInvoiceRepository.save(commercialInvoice);

    }

    @Override
    public Object saveOrUpdateCargoDetails(CargoDetailsRequest cargoDetailsRequest) {

        if (cargoDetailsRepository.existsByCargoDetailsId(cargoDetailsRequest.getCargoDetailsId())) {
            CargoDetails cargoDetails = cargoDetailsRepository.findByCargoDetailsId(cargoDetailsRequest.getCargoDetailsId());
            cargoDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateCargoDetails1(cargoDetails, cargoDetailsRequest);
            return "Updated";
        } else {
            CargoDetails cargoDetails = new CargoDetails();
            cargoDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            cargoDetails.setIsActive(true);
            cargoDetails.setIsDeleted(false);
            cargoDetails.setTrackingId(cargoDetailsRequest.getTrackingId());
            saveOrUpdateCargoDetails1(cargoDetails, cargoDetailsRequest);
            return "Created";
        }
    }

    private void saveOrUpdateCargoDetails1(CargoDetails cargoDetails, CargoDetailsRequest cargoDetailsRequest) {

        cargoDetails.setNote(cargoDetailsRequest.getNote());
        cargoDetails.setPackageType(cargoDetailsRequest.getPackageType());
        cargoDetails.setShipmentReferenceId(cargoDetailsRequest.getShipmentReferenceId());
        cargoDetails.setCargoDetailsFile(cargoDetailsRequest.getCargoDetailsFile());
        CargoDetails cargoDetails1 = cargoDetailsRepository.save(cargoDetails);
        for (BoxDetailsRequest bxr : cargoDetailsRequest.getBoxDetailsRequests()) {
            BoxDetails boxDetails = new BoxDetails();
            boxDetails.setBoxDetailsId(bxr.getBoxDetailsId());
            boxDetails.setTrackingId(cargoDetailsRequest.getTrackingId());
            boxDetails.setCargoDetailsId(cargoDetails1.getCargoDetailsId());
            boxDetails.setHeight(bxr.getHeight());
            boxDetails.setItemName(bxr.getItemName());
            boxDetails.setLength(bxr.getLength());
            boxDetails.setQuantity(bxr.getQuantity());
            boxDetails.setValue(bxr.getValue());
            boxDetails.setWeight(bxr.getWeight());
            boxDetails.setWidth(bxr.getWidth());
            boxDetailsRepository.save(boxDetails);
        }
    }

    @Override
    public Object saveOrUpdateNewShipmentMethods(NewShipmentMethodsRequest newShipmentMethodsRequest) {

        if (newShipmentMethodsRepository.existsByNewShipmentMethodId(newShipmentMethodsRequest.getNewShipmentMethodId())) {
            NewShipmentMethods newShipmentMethods = newShipmentMethodsRepository.findByNewShipmentMethodId(newShipmentMethodsRequest.getNewShipmentMethodId());
            newShipmentMethods.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateNewShipmentMethodsDetails1(newShipmentMethods, newShipmentMethodsRequest);
            return "Updated";
        } else {
            NewShipmentMethods newShipmentMethods = new NewShipmentMethods();
            newShipmentMethods.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            newShipmentMethods.setIsActive(true);
            newShipmentMethods.setIsDeleted(false);
            newShipmentMethods.setTrackingId(newShipmentMethodsRequest.getTrackingId());
            saveOrUpdateNewShipmentMethodsDetails1(newShipmentMethods, newShipmentMethodsRequest);
            return "Created";
        }
    }

    private void saveOrUpdateNewShipmentMethodsDetails1(NewShipmentMethods newShipmentMethods, NewShipmentMethodsRequest newShipmentMethodsRequest) {

        newShipmentMethods.setShipmentMethodOptions(ShipmentMethodOptions.valueOf(newShipmentMethodsRequest.getShipmentMethodOptions()));
        newShipmentMethods.setPromoCode(newShipmentMethodsRequest.getPromoCode());
        newShipmentMethods.setCarrierId(newShipmentMethodsRequest.getCarrierId());
        newShipmentMethods.setCarrierName(newShipmentMethodsRequest.getCarrierName());
        newShipmentMethods.setShipmentReferenceId(newShipmentMethodsRequest.getShipmentReferenceId());
        newShipmentMethods.setCarrierPrice(newShipmentMethodsRequest.getCarrierPrice());
        newShipmentMethods.setService(newShipmentMethodsRequest.getService());
        newShipmentMethodsRepository.save(newShipmentMethods);

    }

    @Override
    public Object saveOrUpdateInsurance(InsuranceRequest insuranceRequest) {

        if (insuranceRepository.existsByInsuranceId(insuranceRequest.getInsuranceId())) {
            Insurance insurance = insuranceRepository.findByInsuranceId(insuranceRequest.getInsuranceId());
            insurance.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdateInsuranceDetails(insurance, insuranceRequest);
            return "Updated";
        } else {
            Insurance insurance = new Insurance();
            insurance.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            insurance.setIsActive(true);
            insurance.setIsDeleted(false);
            insurance.setTrackingId(insuranceRequest.getTrackingId());
            saveOrUpdateInsuranceDetails(insurance, insuranceRequest);
            return "Created";
        }

    }

    private void saveOrUpdateInsuranceDetails(Insurance insurance, InsuranceRequest insuranceRequest) {

        insurance.setInsureFreight(insuranceRequest.getInsureFreight());
        insurance.setShipmentReferenceId(insuranceRequest.getShipmentReferenceId());
        insuranceRepository.save(insurance);
    }

    @Override
    public Object saveOrUpdatePickupScheduler(PickupSchedulerRequest pickupSchedulerRequest) {

        if (pickupSchedulerRepository.existsByPickupSchedulerId(pickupSchedulerRequest.getPickupSchedulerId())) {
            PickupScheduler pickupScheduler = pickupSchedulerRepository.findByPickupSchedulerId(pickupSchedulerRequest.getPickupSchedulerId());
            pickupScheduler.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdatePickupSchedulerDetails(pickupScheduler, pickupSchedulerRequest);
            return "Updated";
        } else {
            PickupScheduler pickupScheduler = new PickupScheduler();
            pickupScheduler.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            pickupScheduler.setIsActive(true);
            pickupScheduler.setIsDeleted(false);
            pickupScheduler.setTrackingId(pickupSchedulerRequest.getTrackingId());
            saveOrUpdatePickupSchedulerDetails(pickupScheduler, pickupSchedulerRequest);
            return "Created";
        }
    }

    private void saveOrUpdatePickupSchedulerDetails(PickupScheduler pickupScheduler, PickupSchedulerRequest pickupSchedulerRequest) {

        pickupScheduler.setShipmentReferenceId(pickupSchedulerRequest.getShipmentReferenceId());
        pickupScheduler.setAddress(pickupSchedulerRequest.getAddress());
        pickupScheduler.setAddress2(pickupSchedulerRequest.getAddress2());
        pickupScheduler.setCity(pickupSchedulerRequest.getCity());
        pickupScheduler.setCompanyName(pickupSchedulerRequest.getCompanyName());
        pickupScheduler.setContactName(pickupSchedulerRequest.getContactName());
        pickupScheduler.setCountry(pickupSchedulerRequest.getCountry());
        pickupScheduler.setEmail(pickupSchedulerRequest.getEmail());
        pickupScheduler.setPhoneNumber(pickupSchedulerRequest.getPhoneNumber());
        pickupScheduler.setPickUpDate(pickupSchedulerRequest.getPickUpDate());
        pickupScheduler.setEarliestPickUpTime(pickupSchedulerRequest.getEarliestPickUpTime());
        pickupScheduler.setLatestPickUpTime(pickupSchedulerRequest.getLatestPickUpTime());
        pickupScheduler.setState(pickupSchedulerRequest.getState());
        pickupScheduler.setZipcode(pickupSchedulerRequest.getZipcode());
        pickupSchedulerRepository.save((pickupScheduler));
    }

    @Override
    public Object getNewShipmentDetailsByTrackingId(String trackingId) {

        List<TrackingDetailsResponse> trackingDetailsResponseList = new ArrayList<>();
        PickupScheduler pickupScheduler = pickupSchedulerRepository.findByTrackingId(trackingId);
        NewShipmentMethods newShipmentMethods = newShipmentMethodsRepository.findByTrackingId(trackingId);
        NewShipmentDetails newShipmentDetails = newShipmentDetailsRepository.findByTrackingId(trackingId);
        List<BoxDetails> boxDetailsList = boxDetailsRepository.findAllByTrackingId(trackingId);

        for (BoxDetails boxDetails : boxDetailsList) {
            String itemName = boxDetails.getItemName();
            TrackingDetailsResponse trackingDetailsResponse = new TrackingDetailsResponse();
            trackingDetailsResponse.setTrackingId(trackingId);
            trackingDetailsResponse.setPickup(pickupScheduler.getPickupSchedulerId());
            trackingDetailsResponse.setCarrier(newShipmentMethods.getCarrierName());
            trackingDetailsResponse.setFrom(newShipmentDetails.getCountry());
            trackingDetailsResponse.setTo(newShipmentDetails.getDeliveryCountry());
            trackingDetailsResponse.setTotal(newShipmentMethods.getCarrierPrice());
            trackingDetailsResponse.setBoxes(boxDetailsRepository.getCountOfBoxDetailsByTrackingId(trackingId, itemName));
            trackingDetailsResponse.setDate(pickupScheduler.getPickUpDate());
            trackingDetailsResponse.setItemShipped(itemName);
            trackingDetailsResponse.setDeliveryStatus(newShipmentDetails.getDeliveryStatus());
            trackingDetailsResponse.setService(newShipmentMethods.getService());
            trackingDetailsResponse.setWeight(boxDetails.getWeight());
            trackingDetailsResponseList.add(trackingDetailsResponse);
        }
        return trackingDetailsResponseList;

        /*trackingDetailsResponse.setNewShipmentDetails(newShipmentDetailsRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setNewShipmentMethods(newShipmentMethodsRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setCommodityDetails(commodityDetailsRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setBoxDetails(boxDetailsRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setCommercialInvoice(commercialInvoiceRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setCargoDetails(cargoDetailsRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setInsurance(insuranceRepository.findByTrackingId(trackingId));
        trackingDetailsResponse.setPickupScheduler(pickupSchedulerRepository.findByTrackingId(trackingId));
        return trackingDetailsResponse;*/
    }

    @Override
    public Object getScheduledPickupDetails(String pickupId) {

        PickupScheduler pickupScheduler = pickupSchedulerRepository.findByPickupSchedulerId(Long.valueOf(pickupId));
        String trackingId = pickupScheduler.getTrackingId();
        List<BoxDetails> boxDetails = boxDetailsRepository.findAllByTrackingId(trackingId);
        NewShipmentMethods newShipmentMethods = newShipmentMethodsRepository.findByTrackingId(trackingId);
        ScheduledPickupResponse scheduledPickupResponse = new ScheduledPickupResponse();
        scheduledPickupResponse.setCompany(userToken.getCompanyFromToken().getName());
        scheduledPickupResponse.setCompanyName(userToken.getCompanyFromToken().getName());
        scheduledPickupResponse.setAddress(pickupScheduler.getAddress());
        scheduledPickupResponse.setBoxes(boxDetails.size());
        scheduledPickupResponse.setCarrier(newShipmentMethods.getCarrierName());
        scheduledPickupResponse.setContactName(pickupScheduler.getContactName());
        scheduledPickupResponse.setCountry(pickupScheduler.getCountry());
        scheduledPickupResponse.setCreatedOn(pickupScheduler.getCreatedAt());
        scheduledPickupResponse.setEarliestPickupTime(pickupScheduler.getEarliestPickUpTime());
        scheduledPickupResponse.setLatestPickupTime(pickupScheduler.getLatestPickUpTime());
        scheduledPickupResponse.setEmail(pickupScheduler.getEmail());
        scheduledPickupResponse.setPhoneNumber(pickupScheduler.getPhoneNumber());
        scheduledPickupResponse.setPickupAddress(pickupScheduler.getAddress());
        scheduledPickupResponse.setPickupDate(pickupScheduler.getPickUpDate());
        scheduledPickupResponse.setPickupNumber(trackingId);
        scheduledPickupResponse.setPickupOn(pickupScheduler.getPickUpDate());
        scheduledPickupResponse.setQuantity(String.valueOf(boxDetails.size()));
        scheduledPickupResponse.setShipmentReferenceId(pickupScheduler.getShipmentReferenceId());
        Double result = 0.0;
        for (BoxDetails bd : boxDetails) {
            Double res = result + Double.valueOf(bd.getWeight());
            scheduledPickupResponse.setBoxWeight(String.valueOf(res));
        }
        return scheduledPickupResponse;
    }

    @Override
    public Object getShipmentByShippingFilter(ShippingFilterRequest shippingFilterRequest) {

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (shippingFilterRequest.getFromDate() != null && !shippingFilterRequest.getFromDate().isEmpty()) {
            LocalDate fromDate = LocalDate.parse(shippingFilterRequest.getFromDate(), formatter);
            fromDateTime = fromDate.atStartOfDay();
        } else if (shippingFilterRequest.getFromDate() != null && shippingFilterRequest.getFromDate().isEmpty()) {
            fromDateTime = null;
        }
        if (shippingFilterRequest.getToDate() != null && !shippingFilterRequest.getToDate().isEmpty()) {
            LocalDate toDate = LocalDate.parse(shippingFilterRequest.getToDate(), formatter);
            toDateTime = toDate.atTime(23, 59, 59);
        } else if (shippingFilterRequest.getToDate() != null && shippingFilterRequest.getToDate().isEmpty()) {
            toDateTime = null;
        }

        DeliveryStatus trackingStatus1 = null;
        String trackingStatus = shippingFilterRequest.getTrackingStatus();
        if (trackingStatus != null && !trackingStatus.isEmpty()) {
            try {
                trackingStatus1 = DeliveryStatus.valueOf(trackingStatus.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Invalid tracking status: " + trackingStatus, e);
            }
        }

        String toCountry = shippingFilterRequest.getToCountry();
        if (toCountry != null && toCountry.isEmpty()) {
            toCountry = null;
        }

        ShipmentMethodOptions deliveryMethod = null;
        String shippingMethod = shippingFilterRequest.getShippingMethod();
        if (shippingMethod != null && !shippingMethod.isEmpty()) {
            try {
                deliveryMethod = ShipmentMethodOptions.valueOf(shippingMethod.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("Invalid tracking status: " + shippingMethod, e);
            }
        }

        String pendingReason = shippingFilterRequest.getPendingReason();
        if (pendingReason != null && pendingReason.isEmpty()) {
            pendingReason = null;
        }

        String fromCountry = shippingFilterRequest.getFromCountry();
        if (fromCountry != null && fromCountry.isEmpty()) {
            fromCountry = null;
        }

        String carrier = shippingFilterRequest.getCarrier();
        if (carrier != null && carrier.isEmpty()) {
            carrier = null;
        }

        String sortBy = shippingFilterRequest.getSortBy();

        try {
            List<FilterResponse> filterResponse = newShipmentDetailsRepository.findByShippingFilter(carrier, fromCountry, fromDateTime, pendingReason,
                    deliveryMethod, toCountry, toDateTime, trackingStatus1, sortBy);

            if ("desc".equalsIgnoreCase(shippingFilterRequest.getSortDirection()) && shippingFilterRequest.getSortDirection() != null) {
                return filterResponse.stream()
                        .sorted((fr1, fr2) -> {
                            if ("trackingStatus".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getStatus().compareTo(fr1.getStatus());
                            } else if ("carrier".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getCarrier().compareTo(fr1.getCarrier());
                            } else if ("service".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getService().compareTo(fr1.getService());
                            } else if ("fromCountry".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getFrom().compareTo(fr1.getFrom());
                            } else if ("toCountry".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getTo().compareTo(fr1.getTo());
                            } else if ("fromDate".equals(shippingFilterRequest.getSortBy()) || "toDate".equals(shippingFilterRequest.getSortBy())) {
                                return fr2.getDate().compareTo(fr1.getDate());
                            } else {
                                return fr2.getTrackingId().compareTo(fr1.getTrackingId());
                            }
                        })
                        .collect(Collectors.toList());
            }

            for (FilterResponse fr : filterResponse) {
                Long count = boxDetailsRepository.getCountOfBoxes(fr.getTrackingId());
                fr.setBoxes(count);
            }

            return filterResponse;
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
        return null;
    }

    @Override
    public Object saveOrUpdateStoreDetails(StoreDetailsRequest storeDetailsRequest) {

        if (storeDetailsRepository.existsById(storeDetailsRequest.getStoreId())) {
            StoreDetails storeDetails = storeDetailsRepository.findById(storeDetailsRequest.getStoreId()).get();
            storeDetails.setIsActive(true);
            storeDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            storeDetails.setIsDeleted(false);
            saveOrUpdateStoreDetails1(storeDetails, storeDetailsRequest);
            return "Updated";
        } else {
            StoreDetails storeDetails = new StoreDetails();
            storeDetails.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            storeDetails.setIsActive(true);
            storeDetails.setIsDeleted(false);
            saveOrUpdateStoreDetails1(storeDetails, storeDetailsRequest);
            return "Created";
        }
    }

    private void saveOrUpdateStoreDetails1(StoreDetails storeDetails, StoreDetailsRequest storeDetailsRequest) {
        storeDetails.setStoreId(storeDetailsRequest.getStoreId());
        storeDetails.setName(storeDetailsRequest.getName());
        storeDetails.setIndustry(storeDetailsRequest.getIndustry());
        storeDetails.setCountry(storeDetailsRequest.getCountry());
        storeDetails.setPhoneNo(storeDetailsRequest.getPhoneNo());
        storeDetails.setParcelCount(storeDetailsRequest.getParcelCount());
        storeDetailsRepository.save(storeDetails);
    }

    @Override
    public Object getStoreDetailsByCompanyId() {

        if (storeDetailsRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            StoreDetails storeDetails = storeDetailsRepository.findByCompanyId(userToken.getCompanyFromToken().getCompanyId());
            if (!storeDetails.getIsDeleted()) {
                return storeDetails;
            } else {
                return "store details are deleted";
            }
        } else {
            return "Not found";
        }
    }

    @Override
    public Object saveCompanyCarriers(Long msfCompanyCarrierId, List<Long> carrierIds) {

        List<MsfCompanyCarriers> list = new ArrayList<>();
        for (Long carrierId : carrierIds) {
            MsfCompanyCarriers msfCompanyCarriers = new MsfCompanyCarriers();
            msfCompanyCarriers.setMsfCompanyCarrierId(msfCompanyCarrierId);
            msfCompanyCarriers.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            msfCompanyCarriers.setIsActive(true);
            msfCompanyCarriers.setIsDeleted(false);
            msfCompanyCarriers.setCarrierId(carrierId);
            list.add(msfCompanyCarriers);
        }
        msfCompanyCarriersRepository.saveAll(list);
        return "Saved";
    }

    @Override
    public Object getAllMsfCompanyCarriers(Pageable pageable) {
        if (msfCompanyCarriersRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            return msfCompanyCarriersRepository.findAllByComapnyId(userToken.getCompanyFromToken().getCompanyId());
        } else {
            return "Not found";
        }
    }

    @Override
    public Object saveOrUpdateBrand(BrandRequest brandRequest) {

        if (brandRepository.existsById(brandRequest.getBrandId())) {
            Brand brand = brandRepository.findByBrandId(brandRequest.getBrandId());
            brand.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            brand.setIsActive(true);
            brand.setIsDeleted(false);
            saveOrUpdateBrandDetails(brand, brandRequest);
            return "Updated";
        } else {
            Brand brand = new Brand();
            brand.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            brand.setIsActive(true);
            brand.setIsDeleted(false);
            saveOrUpdateBrandDetails(brand, brandRequest);
            return "Created";
        }
    }

    private void saveOrUpdateBrandDetails(Brand brand, BrandRequest brandRequest) {
        brand.setBrandId(brandRequest.getBrandId());
        brand.setBrandName(brandRequest.getBrandName());
        brand.setLink(brandRequest.getLink());
        if (brandRequest.getLogo() != null) {
            brand.setLogo(imageUploadService.uploadImage(brandRequest.getLogo()));
        }
        brand.setPrimaryColour(brandRequest.getPrimaryColour());
        brandRepository.save(brand);
    }

    @Override
    public Object getAllBrandsByCompanyId(Pageable pageable) {
        if (brandRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            return brandRepository.findAllByCompanyId(userToken.getCompanyFromToken().getCompanyId(), pageable);
        } else {
            return "Not found";
        }
    }

    @Override
    public Object saveOrUpdatePickUpLocation(PickUpLocationRequest pickUpLocationRequest) {

        if (pickUpLocationRepository.existsById(pickUpLocationRequest.getPickUpLocationId())) {
            PickUpLocation pickUpLocation = pickUpLocationRepository.findByPickUpLocationId(pickUpLocationRequest.getPickUpLocationId());
            pickUpLocation.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            saveOrUpdatePickUpLocationDetails(pickUpLocation, pickUpLocationRequest);
            return "Updated";
        } else {
            PickUpLocation pickUpLocation = new PickUpLocation();
            pickUpLocation.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            pickUpLocation.setIsActive(true);
            pickUpLocation.setIsDeleted(false);
            saveOrUpdatePickUpLocationDetails(pickUpLocation, pickUpLocationRequest);
            return "Created";
        }
    }

    private void saveOrUpdatePickUpLocationDetails(PickUpLocation pickUpLocation, PickUpLocationRequest pickUpLocationRequest) {

        pickUpLocation.setAddress1(pickUpLocationRequest.getAddress1());
        pickUpLocation.setAddress2(pickUpLocationRequest.getAddress2());
        pickUpLocation.setCity(pickUpLocationRequest.getCity());
        pickUpLocation.setCompanyName(pickUpLocationRequest.getCompanyName());
        pickUpLocation.setCountry(pickUpLocationRequest.getCountry());
        pickUpLocation.setEmail(pickUpLocationRequest.getEmail());
        pickUpLocation.setEoriNumber(pickUpLocationRequest.getEoriNumber());
        pickUpLocation.setFirstName(pickUpLocationRequest.getFirstName());
        pickUpLocation.setFullName(pickUpLocationRequest.getFullName());
        pickUpLocation.setInitial(pickUpLocationRequest.getInitial());
        pickUpLocation.setLastName(pickUpLocationRequest.getLastName());
        pickUpLocation.setLossNumber(pickUpLocationRequest.getLossNumber());
        pickUpLocation.setMobileNo(pickUpLocationRequest.getMobileNo());
        pickUpLocation.setPickUpLocationId(pickUpLocationRequest.getPickUpLocationId());
        pickUpLocation.setPostalCode(pickUpLocationRequest.getPostalCode());
        if (pickUpLocationRequest.getSignature() != null) {
            pickUpLocation.setSignature(imageUploadService.uploadImage(pickUpLocationRequest.getSignature()));
        }
        pickUpLocation.setState(pickUpLocationRequest.getState());
        pickUpLocation.setVatNumber(pickUpLocationRequest.getVatNumber());
        pickUpLocationRepository.save(pickUpLocation);
    }

    @Override
    public Object getAllPickUpLocationByCompanyId(Pageable pageable) {

        if (pickUpLocationRepository.existsByCompanyId(userToken.getCompanyFromToken().getCompanyId())) {
            return pickUpLocationRepository.findAllByCompanyId(userToken.getCompanyFromToken().getCompanyId(), pageable);
        } else {
            return "Not found";
        }
    }

    public void sendEmailWithOTP(String email) {

        int otp = otpService.generateOTP(email);
        String body = "OTP for verification :" + otp;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("OTP VERIFICATION");
        message.setText(body);
        javaMailSender.send(message);
    }

    public void sendCredentialEmail(String email, String password) {

        String body = "Welcome to Msf Company.Your signUp was succesfull " +
                "Your credentials are Email: " + email + " and password: " + password +
                " Thank You";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(fromEmail);
        message.setSubject("SignUp: Msf Company");
        message.setText(body);
        javaMailSender.send(message);
    }

    public MsfCompany findByEmail(String email) {
        return msfCompanyRepository.findByEmail(email).get();
    }

    public Boolean isMobNoValid(String mobNo) {
        String regex = "^[0-9]{10}$";
        return mobNo != null && mobNo.matches(regex);
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public String generateTrackingId() {

        String latestTrackingId = newShipmentDetailsRepository.findLatestTrackingId();
        int nextId = 1;
        if (latestTrackingId != null) {
            String numericPart = latestTrackingId.substring(2);
            nextId = Integer.parseInt(numericPart) + 1;
        }
        return String.format("MS%06d", nextId);
    }

//class ends here
}
