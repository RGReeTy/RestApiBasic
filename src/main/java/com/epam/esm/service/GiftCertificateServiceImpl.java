package com.epam.esm.service;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repo.GiftCertificateRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepo giftCertificateRepo;
    private final TagService tagService;

    @Override
    public GiftCertificate findById(Long id) {
        return giftCertificateRepo.findById(id);
    }

    @Override
    public List<GiftCertificate> getAll() {
        return new ArrayList<>(giftCertificateRepo.getAll());
    }

    @Transactional
    @Override
    public Long createGiftCertificate(GiftCertificate giftCertificate) {
        List<Tag> tags = giftCertificate.getTagList();
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        Long insertedGiftCertificateId = giftCertificateRepo.create(giftCertificate);
        if (!Objects.isNull(tags)) {
            List<Long> insertedTagsIds = tags.stream().map(tagService::createTag).collect(Collectors.toList());
            giftCertificateRepo.insertGiftCertificateTagLink(insertedGiftCertificateId, insertedTagsIds);
        }
        return insertedGiftCertificateId;
    }

    @Transactional
    @Override
    public void deleteGiftCertificate(Long id) {
        giftCertificateRepo.delete(id);
    }

    @Override
    public List<GiftCertificate> filterGiftCertificate(Filter filter) {

        List<GiftCertificate> certificates = new ArrayList<>();

        Map<GiftCertificate, List<Tag>> certificateListMap = giftCertificateRepo.filterGiftCertificate(filter);

        certificateListMap.forEach((k, v) -> {
            Stream<Tag> tagStream = v.stream();
            List<Tag> collect = tagStream.collect(Collectors.toList());
            k.setTagList(collect);
            certificates.add(k);
        });

        return certificates;
    }

    @Transactional
    @Override
    public void updateGiftCertificate(GiftCertificate giftCertificate) {

        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepo.update(giftCertificate);

        List<Tag> tags = giftCertificate.getTagList();

        if (!Objects.isNull(tags)) {
            List<Long> insertedTagsIds = tags
                    .stream()
                    .map(tagService::createTag)
                    .collect(Collectors.toList());

            giftCertificateRepo.insertGiftCertificateTagLink(giftCertificate.getId(), insertedTagsIds);
        }
    }

    @Override
    public void linkTagToGiftCertificate(Long certificateId, Long tagId) {
        giftCertificateRepo.findById(certificateId);
        tagService.getTag(tagId);
        giftCertificateRepo.insertGiftCertificateTagLink(certificateId, Collections.singletonList(tagId));
    }
}
