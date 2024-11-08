package com.example.user_verification.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ListOfCommodityDetailsRequest {

    private List<CommodityDetailsRequest> commodityDetailsRequestList;

}
