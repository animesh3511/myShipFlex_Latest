package com.example.user_verification.model.request;

import com.example.user_verification.model.PackageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CargoDetailsRequest {

    private Long cargoDetailsId;
    private Long shipmentReferenceId;
    private String trackingId;
    private PackageType packageType;
    private String cargoDetailsFile;
    private String note;
    private List<BoxDetailsRequest> boxDetailsRequests;

}
