package com.epam.esm.repo;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateRepo {

    Long create(GiftCertificate certificate);

    GiftCertificate findById(Long id);

    List<GiftCertificate> getAll();

    void insertGiftCertificateTagLink(Long certificateId, List<Long> tagsId);

    void update(GiftCertificate giftCertificate);

    void addTagToCertificate(Long certificateId, List<Long> tagsId);

    void delete(Long id);

    Map<GiftCertificate, List<Tag>> filterGiftCertificate(Filter filter);
}
