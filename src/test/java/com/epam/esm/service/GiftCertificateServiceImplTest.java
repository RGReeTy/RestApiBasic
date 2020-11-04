package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.mapper.GiftCertificateMapper;
import com.epam.esm.repo.GiftCertificateRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceImplTest {

    @Mock
    private GiftCertificateRepo mockGiftCertificateRepo;
    @Mock
    private GiftCertificateMapper certificateMapper;
    @Mock
    private TagService mockTagService;
    @InjectMocks
    private GiftCertificateService certificateService;


    @Test
    void getOne() {
        GiftCertificate certificate = GiftCertificate.builder()
                .id(1L).name("name")
                .description("description")
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now()).duration(5)
                .build();

        GiftCertificateDto certificateDto = GiftCertificateDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now()).duration(5)
                .build();

        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockGiftCertificateRepo.findById(1L)).thenReturn(certificate);
        Assertions.assertEquals(certificateDto, certificateService.findById(1L));
    }

    @Test
    void getAllTags() {
        GiftCertificate certificate = GiftCertificate.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        GiftCertificateDto certificateDto = GiftCertificateDto.builder().id(1L).name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        List<GiftCertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        List<GiftCertificate> certificateList = Collections.singletonList(certificate);
        Mockito.when(certificateMapper.fromModel(certificate)).thenReturn(certificateDto);
        Mockito.when(mockGiftCertificateRepo.getAll()).thenReturn(certificateList);
        Assertions.assertEquals(certificateDtoList, certificateService.getAll());
    }

    @Test
    void deleteCertificate() {
        Mockito.doNothing().when(mockGiftCertificateRepo).delete(5L);
        certificateService.deleteGiftCertificate(5L);
        Mockito.verify(mockGiftCertificateRepo, times(1)).delete(5L);
    }

    @Test
    void createCertificate() {
        GiftCertificate certificate = GiftCertificate.builder().name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Tag boatDto = Tag.builder().name("boat").build();
        certificate.setTagList(Collections.singletonList(boatDto));
        GiftCertificateDto certificateDto = GiftCertificateDto.builder().name("name").description("description")
                .createDate(LocalDateTime.now()).lastUpdateDate(LocalDateTime.now()).duration(5).build();
        TagDto boat = TagDto.builder().name("boat").build();
        certificateDto.setTags(Collections.singletonList(boat));

        List<Long> insertedTagsId = new ArrayList<>();
        insertedTagsId.add(7L);

        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.when(mockGiftCertificateRepo.create(certificate)).thenReturn(5L);
        Mockito.when(mockTagService.createTag(boat)).thenReturn(7L);
        Mockito.doNothing().when(mockGiftCertificateRepo).insertGiftCertificateTagLink(5L, insertedTagsId);
        Assertions.assertEquals(5L, certificateService.createGiftCertificate(certificateDto));
    }

    @Test
    void updateCertificate() {
        GiftCertificate certificate = GiftCertificate.builder().id(11L).name("name").description("description")
                .lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Tag boatDto = Tag.builder().name("boat").build();
        certificate.setTagList(Collections.singletonList(boatDto));
        GiftCertificateDto certificateDto = GiftCertificateDto.builder().id(11L).name("name").description("description")
                .duration(5).build();
        TagDto boat = TagDto.builder().name("boat").build();
        certificateDto.setTags(Collections.singletonList(boat));
        List<Long> insertedTagsId = new ArrayList<>();
        insertedTagsId.add(7L);

        Mockito.when(certificateMapper.toModel(certificateDto)).thenReturn(certificate);
        Mockito.doNothing().when(mockGiftCertificateRepo).update(certificate);
        Mockito.when(mockTagService.createTag(boat)).thenReturn(7L);
        Mockito.doNothing().when(mockGiftCertificateRepo).insertGiftCertificateTagLink(11L, insertedTagsId);

        certificateService.updateGiftCertificate(certificateDto);

        Mockito.verify(mockGiftCertificateRepo, times(1)).update(certificate);
        Mockito.verify(mockGiftCertificateRepo,
                times(1)).insertGiftCertificateTagLink(11L, insertedTagsId);
    }

}