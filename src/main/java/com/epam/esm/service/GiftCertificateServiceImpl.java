package com.epam.esm.service;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.FilterDto;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.mapper.FilterMapper;
import com.epam.esm.model.dto.mapper.GiftCertificateMapper;
import com.epam.esm.model.dto.mapper.TagMapper;
import com.epam.esm.repo.GiftCertificateRepo;
import lombok.RequiredArgsConstructor;
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
    private final GiftCertificateMapper giftCertificateMapper;
    private final FilterMapper filterMapper;
    private final TagMapper tagMapper;

    @Override
    public GiftCertificateDto findById(Long id) {
        return giftCertificateMapper.fromModel(giftCertificateRepo.findById(id));
    }

    @Override
    public List<GiftCertificateDto> getAll() {
        return giftCertificateRepo
                .getAll()
                .stream()
                .map(giftCertificateMapper::fromModel)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Long createGiftCertificate(GiftCertificateDto giftCertificate) {
        List<TagDto> tags = giftCertificate.getTags();
        GiftCertificate model = giftCertificateMapper.toModel(giftCertificate);
        model.setCreateDate(LocalDateTime.now());
        model.setLastUpdateDate(LocalDateTime.now());
        Long insertedGiftCertificateId = giftCertificateRepo.create(model);

        if (!Objects.isNull(tags)) {
            List<Long> insertedTagsIds = tags
                    .stream()
                    .map(tagService::createTag)
                    .collect(Collectors.toList());

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
    public List<GiftCertificateDto> filterGiftCertificate(FilterDto filterDto) {

        Filter filter = filterMapper.toModel(filterDto);
        List<GiftCertificateDto> certificates = new ArrayList<>();
        Map<GiftCertificate, List<Tag>> certificateListMap = giftCertificateRepo.filterGiftCertificate(filter);
        certificateListMap.forEach((k, v) -> {
            Stream<TagDto> tagDtoStream = v.stream().map(tagMapper::fromModelWithoutCertificate);
            List<TagDto> collect = tagDtoStream.collect(Collectors.toList());
            GiftCertificateDto certificateDto = giftCertificateMapper.fromModel(k);
            certificateDto.setTags(collect);
            certificates.add(certificateDto);
        });

        return certificates;
    }

    @Transactional
    @Override
    public void updateGiftCertificate(GiftCertificateDto giftCertificate) {
        GiftCertificate certificate = giftCertificateMapper.toModel(giftCertificate);
        certificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificateRepo.update(certificate);
        List<TagDto> tags = giftCertificate.getTags();
        if (!Objects.isNull(tags)) {
            List<Long> insertedTagsIds = tags.stream().map(tagService::createTag).collect(Collectors.toList());
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
