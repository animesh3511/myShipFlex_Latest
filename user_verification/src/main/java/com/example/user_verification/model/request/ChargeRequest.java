package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChargeRequest {

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public String getProductName() {
        return productName;
    }

    private Long amount;

    public Long getAmount() {
        return amount;
    }

    private String productName;

    public String getEmail() {
        return email;
    }


}
