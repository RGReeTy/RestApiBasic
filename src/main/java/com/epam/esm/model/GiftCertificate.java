package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class GiftCertificate {

    private String name;
    private String description;
    private BigDecimal price;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;
    //Duration - in days (expiration period)
    private int duration;

}
