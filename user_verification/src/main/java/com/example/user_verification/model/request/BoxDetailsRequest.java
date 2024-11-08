package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BoxDetailsRequest {

    private Long boxDetailsId;
    private String quantity;
    private String trackingId;
    private String itemName;
    private String weight;
    private String length;
    private String width;
    private String height;
    private String value;


}
