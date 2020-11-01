package com.epam.esm.service;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {


    GiftCertificate findById(Long id);

    List<GiftCertificate> getAll();

    Long createGiftCertificate(GiftCertificate certificate);

    void deleteGiftCertificate(Long id);

    List<GiftCertificate> filterGiftCertificate(Filter filter);

    void updateGiftCertificate(GiftCertificate giftCertificate);

    void linkTagToGiftCertificate(Long certificateId, Long tagId);
}
