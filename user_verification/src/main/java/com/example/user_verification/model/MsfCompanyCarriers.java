package com.example.user_verification.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "msf_company_carriers")
public class MsfCompanyCarriers {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "msf_company_carrier_id")
    private Long msfCompanyCarrierId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "isActive")
    private Boolean isActive;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
