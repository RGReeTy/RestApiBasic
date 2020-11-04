package com.epam.esm.service;

import com.epam.esm.model.dto.FilterDto;
import com.epam.esm.model.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {


    GiftCertificateDto findById(Long id);

    List<GiftCertificateDto> getAll();

    Long createGiftCertificate(GiftCertificateDto certificate);

    void deleteGiftCertificate(Long id);

    List<GiftCertificateDto> filterGiftCertificate(FilterDto filter);

    void updateGiftCertificate(GiftCertificateDto giftCertificate);

    void linkTagToGiftCertificate(Long certificateId, Long tagId);
}
