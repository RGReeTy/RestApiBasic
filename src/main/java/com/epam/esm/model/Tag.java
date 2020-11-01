package com.epam.esm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag implements Serializable {

    private static final long serialVersionUID = -4186154800532660410L;

    private long id;
    private String name;
    private Set<GiftCertificate> certificates = new HashSet<>();
}
