package com.example.user_verification.controller;

import com.example.user_verification.model.CardData;
import com.example.user_verification.model.ChargeDetails;
import com.example.user_verification.model.CustomerData;
import com.example.user_verification.model.PaymentMethodData;
import com.example.user_verification.model.request.ChargeRequest;
import com.example.user_verification.repository.CardDataRepository;
import com.example.user_verification.repository.ChargeDetailsRepository;
import com.example.user_verification.repository.CustomerDataRepository;
import com.example.user_verification.service.MsfCompanyService;
import com.example.user_verification.serviceimpl.UserToken;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.ChargeListParams;
import com.stripe.param.PaymentMethodListParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.validation.Valid;
import java.util.*;

@Controller
public class StripePaymentController {

    @Value("${stripe.apikey}")
    String stripeKey;

    @Value("${stripe.publickey}")
    private String publicKey;

    @Value("${stripe.currency}")
    private String currency;

    @Autowired
    private CustomerDataRepository customerDataRepository;
    @Autowired
    private CardDataRepository cardDataRepository;
    @Autowired
    private ChargeDetailsRepository chargeDetailsRepository;
    @Autowired
    private UserToken userToken;

    SecretKey secretKey = generateKey();

    public StripePaymentController() throws Exception {
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/checkout")
    public String checkout() {
        return "checkout";
    }

    @RequestMapping("/index1")
    public String index(Model model) {
        model.addAttribute("ChargeRequest", new ChargeRequest());
        return "index1";
    }

    @RequestMapping("/createCustomer")
    public String createCustomer(@ModelAttribute CustomerData customerData) {
        return "createCustomer";
    }

    @RequestMapping("/add-card")
    public String addCard() {
        return "add-card";
    }


    /*@RequestMapping("/addCard")
    public String addCard() {
        return "addCard";
    }*/

    @RequestMapping("/customer")
    public String customer(Model model) throws StripeException {

        Stripe.apiKey = stripeKey;
        Map<String, Object> params = new HashMap<>();
        params.put("limit", 20);
        CustomerCollection customers = Customer.list(params);
        List<CustomerData> allCustomer = new ArrayList<>();
        for (int i = 0; i < customers.getData().size(); i++) {
            CustomerData customerData = new CustomerData();
            customerData.setCustomerId(customers.getData().get(i).getId());
            customerData.setName(customers.getData().get(i).getName());
            customerData.setEmail(customers.getData().get(i).getEmail());
            // customerData.setCompanyId(userToken.getCompanyFromToken().getCompanyId());
            allCustomer.add(customerData);
        }
        model.addAttribute("customers", allCustomer);
        return "customer";
    }


    @RequestMapping("/customer/{customerId}/chargeDetails")
    public String chargeDetails(@PathVariable String customerId, Model model) throws Exception {
        Stripe.apiKey = stripeKey;
        ChargeListParams params = ChargeListParams.builder()
                .setCustomer(customerId)
                .build();
        ChargeCollection charges = Charge.list(params);
        Customer customer = Customer.retrieve(customerId);
        List<ChargeDetails> chargeDetailsList = new ArrayList<>();
        for (int i = 0; i < charges.getData().size(); i++) {
            ChargeDetails chargeDetails = new ChargeDetails();
            chargeDetails.setAmount(charges.getData().get(i).getAmount());
            chargeDetails.setPaid(charges.getData().get(i).getPaid());
            chargeDetails.setName(customer.getName());
            chargeDetails.setEmail(customer.getEmail());
            chargeDetails.setChargeId(charges.getData().get(i).getId());
            chargeDetails.setLast4(charges.getData().get(i).getPaymentMethodDetails().getCard().getLast4());
            chargeDetails.setCard(charges.getData().get(i).getPaymentMethodDetails().getCard().getBrand());
            chargeDetailsList.add(chargeDetails);
            if (chargeDetailsRepository.existsByChargeId(charges.getData().get(i).getId())) {
                System.out.println("charge details are up to date");
            } else {
                chargeDetailsRepository.save(chargeDetails);
            }
        }
        model.addAttribute("customerId", customerId);
        model.addAttribute("chargeDetails", chargeDetailsList);
        return "chargeDetails";
    }

    @RequestMapping("/customer/{customerId}/cards")
    public String customerCards(@PathVariable String customerId, Model model) throws Exception {
        Stripe.apiKey = stripeKey;

        List<PaymentMethod> paymentMethods = PaymentMethod.list(
                PaymentMethodListParams.builder()
                        .setCustomer(customerId)
                        .setType(PaymentMethodListParams.Type.CARD)
                        .build()
        ).getData();
        for (PaymentMethod paymentMethod : paymentMethods) {
            System.out.println("Payment Method: " + paymentMethod);
            if (paymentMethod.getCard() != null) {
                System.out.println("Card Details: " + paymentMethod.getCard());
            }
        }

        for (PaymentMethod paymentMethod : paymentMethods) {
            if (cardDataRepository.existsByFingerPrintAndCustomerId(paymentMethod.getCard().getFingerprint(),
                    customerId)) {
                System.out.println("card details are up do date");
            } else {
                CardData cardData = new CardData();
                cardData.setCustomerId(customerId);
                Customer customer = Customer.retrieve(customerId);
                cardData.setCustomerName(customer.getName());
                cardData.setBrand(paymentMethod.getCard().getBrand());
                cardData.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
                cardData.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));
                String encryptedData = encrypt(paymentMethod.getCard().getLast4(), secretKey);
                cardData.setLast4(encryptedData);
                cardData.setFingerPrint(paymentMethod.getCard().getFingerprint());
                cardDataRepository.save(cardData);
            }
        }

        List<CardData> cardDataList = new ArrayList<>();
        for (PaymentMethod paymentMethod : paymentMethods) {
            CardData cardData = new CardData();
            cardData.setBrand(paymentMethod.getCard().getBrand());
            cardData.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
            cardData.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));
            String encryptedData = encrypt(paymentMethod.getCard().getLast4(), secretKey);
            String decryptedData = decrypt(encryptedData, secretKey);
            cardData.setLast4(decryptedData);
            cardDataList.add(cardData);
        }

        model.addAttribute("cu" +
                "stomerId", customerId);
        model.addAttribute("cardData", cardDataList);
        return "customerCards";
    }

    /*@RequestMapping("/createPayment")
    public String createPayment(@ModelAttribute PaymentMethodData paymentMethodData) {
        return "createPayment";
    }*/

    @RequestMapping("/addCustomer")
    public String addCustomer(@ModelAttribute CustomerData customerData, Model model) throws StripeException {
        Stripe.apiKey = stripeKey;
        Map<String, Object> params = new HashMap<>();
        params.put("name", customerData.getName());
        params.put("email", customerData.getEmail());
        if ((customerData.getName() != null && !customerData.getName().isEmpty()) && (customerData.getEmail() != null
                && !customerData.getEmail().isEmpty())) {
            Customer customer = Customer.create(params);
            System.out.println("customer id of " + customerData.getEmail() + " is " + customer.getId());
            CustomerData customerData1 = new CustomerData();
            customerData1.setName(customerData.getName());
            customerData1.setEmail(customerData.getEmail());
            customerData1.setCustomerId(customer.getId());
            customerDataRepository.save(customerData1);
            return "success";
        } else {
            return null;
        }
       /* String paymentMethodId = (String) model.getAttribute("paymentMethodId");
        customerData.setPaymentMethodId(paymentMethodId);
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> attachParams = new HashMap<>();
        attachParams.put("customer", customer.getId());
        paymentMethod.attach(attachParams);*/

    }

    @RequestMapping("/deleteCustomer/{customerId}")
    public String deleteCustomer(@PathVariable String customerId, Model model) throws Exception {
        Stripe.apiKey = stripeKey;
        Customer customer = Customer.retrieve(customerId);
        Customer deletedCustomer = customer.delete();
        if (customerDataRepository.existsByCustomerId(customerId)) {
            CustomerData customerData = customerDataRepository.findByCustomerId(customerId);
            customerDataRepository.delete(customerData);
            return "success";
        } else {
            throw new Exception("Not Found");
        }
    }

    @RequestMapping("/detachCard/{customerId}/{cardLast4}")
    public String detachCard(@PathVariable String customerId, @PathVariable String cardLast4, Model model) throws Exception {
        Stripe.apiKey = stripeKey;
        Customer customer = Customer.retrieve(customerId);
        List<PaymentMethod> paymentMethods = PaymentMethod.list(
                PaymentMethodListParams.builder()
                        .setCustomer(customerId)
                        .setType(PaymentMethodListParams.Type.CARD)
                        .build()
        ).getData();

        for (PaymentMethod paymentMethod : paymentMethods) {
            if (paymentMethod.getCard().getLast4().equals(cardLast4)) {
                paymentMethod.detach();
                CardData cardData1 = cardDataRepository.findByCustomerId(customerId);
                System.out.println("Encrypted data" + cardData1.getLast4());
                String encryptedLast4 = cardData1.getLast4();
                String cardLast41 = decrypt(encryptedLast4, secretKey);
                System.out.println("cardLast4_1 " + cardLast41);
                CardData cardData = cardDataRepository.findByLast4AndCustomerId(encryptedLast4, customerId);

                cardDataRepository.delete(cardData);
                return "success";
            } else {
                System.out.println(paymentMethod.getCard().getLast4() + " card last4 " + cardLast4);
                continue;
            }
        }
        return "success";
    }


   /* @RequestMapping("/addCard")
    public String addCard(@ModelAttribute CardData cardData, @RequestParam("stripeToken") String stripeToken) throws StripeException {
        Stripe.apiKey = stripeKey;

        // Use the token to create a new card source
        CustomerData customerData = customerDataRepository.findByEmail(cardData.getEmail());
        Customer cust = Customer.retrieve(customerData.getCustomerId());
        Map<String, Object> source = new HashMap<>();
        source.put("source", stripeToken);
        cust.getSources().create(source);

        // Save card data to your database if needed
        // ...

        return "success";
    }
*/

    /*@RequestMapping("/addCard")
    public String addCard(@ModelAttribute CardData cardData) throws StripeException {
        Stripe.apiKey = stripeKey;

        System.out.println("email of customer of carddata => " + cardData.getEmail());
        CustomerData customerData = customerDataRepository.findByEmail(cardData.getEmail());
        System.out.println("Customer email => " + customerData.getEmail());
        System.out.println("Customer id => " + customerData.getCustomerId());
        Customer cust = Customer.retrieve(customerData.getCustomerId());
        Map<String, Object> cardParam = new HashMap<>();
        cardParam.put("number", cardData.getCardNumber());
        cardParam.put("exp_month", cardData.getExpMonth());
        cardParam.put("exp_year", cardData.getExpYear());
        cardParam.put("cvc", cardData.getCvc());
        Map<String, Object> tokenParam = new HashMap<>();
        tokenParam.put("card", cardParam);
        Token token = Token.create(tokenParam);
        Map<String, Object> source = new HashMap<>();
        source.put("source", token);
        cust.getSources().create(source);

        CardData cardData1 = new CardData();
        cardData1.setCustomerId(customerData.getCustomerId());
        cardData1.setCardNumber(cardData.getCardNumber());
        cardData1.setExpMonth(cardData.getExpMonth());
        cardData1.setExpYear(cardData.getExpYear());
        cardData1.setCvc(cardData.getCvc());
        cardData1.setEmail(cardData.getEmail());
        cardDataRepository.save(cardData1);

        return "success";
    }
*/
/*
    @PostMapping("/createSetupIntent")
    public ResponseEntity<Map<String, String>> createSetupIntent(@RequestBody String customerId) throws StripeException {
        // Set your secret key
        Stripe.apiKey = stripeKey;
        // Create Setup Intent
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        SetupIntent setupIntent = SetupIntent.create(params);
        // Return the client secret
        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", setupIntent.getClientSecret());
        return ResponseEntity.ok(response);
    }*/


/*
    @PostMapping("/addCard")
    public Object addCard(@RequestBody Map<String, String> requestData) throws StripeException {
        // Add Stripe key
        Stripe.apiKey = stripeKey;

        String paymentMethodId = requestData.get("paymentMethodId");
        String customerId = requestData.get("customerId");

        // Retrieve customer
        Customer customer = Customer.retrieve(customerId);

        // Attach payment method to customer
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        PaymentMethod.attach(paymentMethodId, params);
        return "Card added successfully";
    }*/


    @PostMapping("/attach-payment-method")
    public ResponseEntity<?> attachPaymentMethod(@RequestBody @Valid Map<String, String> data) {

        System.out.println("Inside attach payment method");
        String email = data.get("email");
        CustomerData customerData = customerDataRepository.findByEmail(email);
        String customerId = customerData.getCustomerId();
        String paymentMethodId = data.get("payment_method_id");
        Stripe.apiKey = stripeKey;
        /*RequestOptions requestOptions = RequestOptions.builder()
                .setIdempotencyKey(customerId + paymentMethodId)
                .build();*/
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        //params.put("payment_method", paymentMethodId);
        //PaymentMethodAttachment attachment = PaymentMethodAttachment.create(params);
        try {
            PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
            paymentMethod.attach(params);
            return ResponseEntity.ok(Collections.singletonMap("message", "Payment method attached successfully"));
        } catch (StripeException e) {

            System.out.println("Stripe API Error: " + e.getMessage());
            System.out.println("Stripe API Error Code: " + e.getStatusCode());
            System.out.println("Stripe API Error Type: " + e.getStripeError().getType());
            System.out.println("Stripe API Error Param: " + e.getStripeError().getParam());
            //return "error-attaching-payment-method";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Failed to attach payment method: " + e.getMessage()));
        }
    }

    @PostMapping("/showCard")
    public String showCard(@ModelAttribute @Valid ChargeRequest chargeRequest,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        model.addAttribute("publicKey", publicKey);
        model.addAttribute("amount", chargeRequest.getAmount());
        model.addAttribute("email", chargeRequest.getEmail());
        model.addAttribute("productName", chargeRequest.getProductName());
        return "checkout";
    }


    /*@RequestMapping("/getAllPaymentMethods")
    public String paymentMethods(Model model) throws StripeException {
        Stripe.apiKey = "sk_test_51Py4ib082jp7x4Erpgw4fq75qOFbN1JwYFwh0TzHQiG4xsgmivrcBmm74p3tL40yFCqSlziILIJbV7Ore5AHzlIS00TIVe1rGs";

      *//*  PaymentMethodListParams params = PaymentMethodListParams.builder()
                .setLimit(100L)
                .build();*//*

     *//*Map<String, Object> params = new HashMap<>();
        params.put("limit", 100);*//*

        PaymentMethodListParams params =
                PaymentMethodListParams.builder()
                        //.setType(PaymentMethodListParams.Type.CARD)
                        .setLimit(100L)
                        .setCustomer("cus_Qpr2DXPSFApwpA")
                        .build();
        try {
            PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
            // PaymentMethodCollection paymentMethods = PaymentMethod.list(params);
            List<PaymentMethodData> paymentMethodDataList = new ArrayList<>();
            System.out.println(" => " + paymentMethods.getData().size());

            for (PaymentMethod paymentMethod : paymentMethods.getData()) {
                PaymentMethodData paymentMethodData = new PaymentMethodData();
                paymentMethodData.setPaymentMethodId(paymentMethod.getId());
                paymentMethodData.setCustomerId(paymentMethod.getCustomer());
                paymentMethodData.setPaymentMethodType(paymentMethod.getType());
                if (paymentMethod.getType().equals("card")) {
                    PaymentMethod.Card card = paymentMethod.getCard();
                    paymentMethodData.setCardBrand(card.getBrand());
                }
                Customer customer = Customer.retrieve(paymentMethod.getCustomer());
                paymentMethodData.setCustomerEmail(customer.getEmail());
                paymentMethodDataList.add(paymentMethodData);
            }
            for (PaymentMethodData paymentMethodData : paymentMethodDataList) {
                System.out.println("=> " + paymentMethodData);
            }
            model.addAttribute("paymentMethods", paymentMethodDataList);
        } catch (StripeException e) {
            System.err.println("Error retrieving payment methods: " + e.getMessage());
        }
        return "paymentMethods";
    }
*/

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    public static String encrypt(String data, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
        byte[] combined = new byte[iv.length + encryptedData.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);
        return Base64.getEncoder().encodeToString(combined);
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(encryptedData);
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[combined.length - iv.length];
        System.arraycopy(combined, 0, iv, 0, iv.length);
        System.arraycopy(combined, iv.length, cipherText, 0, cipherText.length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] originalData = cipher.doFinal(cipherText);
        return new String(originalData, "UTF-8");
    }

}
