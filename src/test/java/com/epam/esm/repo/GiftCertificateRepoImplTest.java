package com.epam.esm.repo;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.repo.mapper.CertificateExtractor;
import com.epam.esm.repo.mapper.CertificateRowMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.time.LocalDateTime;
import java.util.List;

public class GiftCertificateRepoImplTest {

    private EmbeddedDatabase embeddedDatabase;
    private JdbcTemplate jdbcTemplate;
    private CertificateRowMapper tagRowMapper;
    private CertificateExtractor certificateExtractor;
    private GiftCertificateRepo giftCertificateRepo;

    @Before
    public void setUp() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .addDefaultScripts()
                .setType(EmbeddedDatabaseType.H2)
                .build();

        this.jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        this.tagRowMapper = new CertificateRowMapper();
        this.certificateExtractor = new CertificateExtractor();
        this.giftCertificateRepo = new GiftCertificateRepoImpl(jdbcTemplate, tagRowMapper, certificateExtractor);
    }

    @After
    public void tearDown() {
        embeddedDatabase.shutdown();
    }

    @Test
    public void testFindOnePositive() {
        Assertions.assertNotNull(giftCertificateRepo.findById(121L));
    }

    @Test
    public void testFindOneNegative() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> giftCertificateRepo.findById(1111111L));
    }

    @Test
    public void getAllCertificates() {
        Assertions.assertNotNull(giftCertificateRepo.getAll());
        Assertions.assertEquals(4, giftCertificateRepo.getAll().size());
    }

    @Test
    public void insertCertificate() {
        GiftCertificate certificate = GiftCertificate.builder()
                .name("name")
                .description("description")
                .createDate(LocalDateTime.now())
                .lastUpdateDate(LocalDateTime.now()).duration(5).build();
        Long id = giftCertificateRepo.create(certificate);

        Assertions.assertNotNull(giftCertificateRepo.findById(id));
    }

    @Test
    public void deleteCertificate() {
        Assertions.assertNotNull(giftCertificateRepo.findById(124L));
        giftCertificateRepo.delete(124L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> giftCertificateRepo.findById(124L));
    }

    @Test
    public void updateCertificate() {
        GiftCertificate updated = GiftCertificate.builder()
                .id(124L)
                .lastUpdateDate(LocalDateTime.now())
                .name("updatedName")
                .build();

        giftCertificateRepo.update(updated);
        Assertions.assertEquals(updated.getName(), giftCertificateRepo.findById(124L).getName());
    }

    @Test
    public void filterCertificate_OptionalParams_OneCertificate() {
        Filter filter = Filter.builder().searchDescription("sea").tagName("tag6").build();
        Assertions.assertNotNull(giftCertificateRepo.filterGiftCertificate(filter));
    }

    @Test
    public void filterCertificate_OptionalParams_SeveralCertificates() {
        Filter filter = Filter.builder().searchName("pd").build();
        Assertions.assertEquals(4, giftCertificateRepo.filterGiftCertificate(filter).size());
    }

    @Test
    public void filterCertificate_AllParams() {
        Filter filter = Filter.builder()
                .sort("name")
                .order("ASC")
                .searchDescription("boat")
                .tagName("tag3")
                .searchName("pd2").build();
        Assertions.assertNotNull(giftCertificateRepo.filterGiftCertificate(filter));
    }

    @Test
    public void deleteCertificateLink() {
        Assertions.assertNotNull(giftCertificateRepo.findById(124L));
        Assertions.assertThrows(Exception.class, () -> giftCertificateRepo.delete(124L));
        giftCertificateRepo.delete(124L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> giftCertificateRepo.findById(124L));
    }

    @Test
    public void insertCertificateTagLink() {
        Long certificateId = 124L;
        List<Long> tagsId = List.of(111L, 112L);
        giftCertificateRepo.insertGiftCertificateTagLink(certificateId, tagsId);
        String query = "SELECT count(`certificate_id`) FROM `certificate_has_tag` WHERE certificate_id=?";
        int rowCount = this.jdbcTemplate.queryForObject(query, Integer.class, 124);
        Assertions.assertEquals(3, rowCount);
    }

}