package com.epam.esm.repo;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repo.mapper.CertificateExtractor;
import com.epam.esm.repo.mapper.CertificateRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class GiftCertificateRepoImpl implements GiftCertificateRepo {

    private static final String GET_BY_ID = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration FROM " +
            "certificate WHERE certificate.id=?";
    private static final String GET_All = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration FROM " +
            "certificate";
    private static final String INSERT_CERTIFICATE = "INSERT INTO `certificate` (`name`, `description`, `createDate`, " +
            "`lastUpdateDate`, `duration`) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_CERTIFICATE = "DELETE FROM `certificate` WHERE id=?";
    private static final String DELETE_CERTIFICATE_LINK = "DELETE FROM `certificate_has_tag` WHERE certificate_id=?";
    private static final String INSERT_CERTIFICATE_TAG_LINK = "INSERT INTO `certificate_has_tag`" +
            "(`certificate_id`, `tag_id`) VALUES (?,?)";
    private static final String GET_BY_FILTER = "SELECT certificate.id c_id,certificate.name " +
            "c_name, certificate.description c_description, certificate.createDate c_createDate, " +
            "certificate.lastUpdateDate c_lastUpdateDate, certificate.duration c_duration, tag.id t_id, " +
            "tag.name t_name FROM certificate " +
            "left join certificate_has_tag on certificate_has_tag.certificate_id=certificate.id " +
            "left join tag on certificate_has_tag.tag_id=tag.id  WHERE 1";
    private static final String UPDATE_CERTIFICATE = "UPDATE `certificate` SET";

    private final JdbcTemplate jdbcTemplate;
    private final CertificateRowMapper certificateRowMapper;
    private final CertificateExtractor certificateExtractor;


    @Override
    public GiftCertificate findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id}, certificateRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Resource not found", id);
        }
    }

    @Override
    public List<GiftCertificate> getAll() {
        try {
            return jdbcTemplate.query(GET_All, certificateRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Resource not found");
        }
    }

    @Override
    public Long create(GiftCertificate certificate) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_CERTIFICATE, new String[]{"id"});
                    ps.setString(1, certificate.getName());
                    ps.setString(2, certificate.getDescription());
                    ps.setTimestamp(3, Timestamp.valueOf(certificate.getCreateDate()));
                    ps.setTimestamp(4, Timestamp.valueOf(certificate.getLastUpdateDate()));
                    ps.setInt(5, certificate.getDuration());
                    return ps;
                },
                keyHolder);
        return (Long) keyHolder.getKey();
    }

    @Override
    public void insertGiftCertificateTagLink(Long certificateId, List<Long> tagsId) {
        tagsId.forEach(e -> this.jdbcTemplate.update(INSERT_CERTIFICATE_TAG_LINK, certificateId, e));
    }

    @Override
    public void update(GiftCertificate giftCertificate) {

        List<Object> data = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UPDATE_CERTIFICATE);
        stringBuilder.append(" lastUpdateDate=?,");
        data.add(giftCertificate.getLastUpdateDate());

        if (!Objects.isNull(giftCertificate.getName())) {
            stringBuilder.append(" name=?,");
            data.add(giftCertificate.getName());
        }

        if (!Objects.isNull(giftCertificate.getDescription())) {
            stringBuilder.append(" description=?,");
            data.add(giftCertificate.getDescription());
        }

        if (giftCertificate.getDuration() != 0) {
            stringBuilder.append(" duration=?,");
            data.add(giftCertificate.getDuration());
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(" WHERE id=?");
        data.add(giftCertificate.getId());

        this.jdbcTemplate.update(String.valueOf(stringBuilder), data.toArray());
    }

    @Override
    public void addTagToCertificate(Long certificateId, List<Long> tagsId) {
        tagsId.forEach(e -> this.jdbcTemplate.update(INSERT_CERTIFICATE_TAG_LINK, certificateId, e));
    }


    @Override
    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_CERTIFICATE, id);
    }

    @Override
    public Map<GiftCertificate, List<Tag>> filterGiftCertificate(Filter filter) {
        try {
            return jdbcTemplate.query(createFilterQuery(filter), certificateExtractor);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Resource not found");
        }
    }


    private String createFilterQuery(Filter filter) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(GET_BY_FILTER);

        if (!Objects.isNull(filter.getTagName()))
            stringBuilder.append(" AND tag.name = '").append(filter.getTagName()).append("'");

        if (!Objects.isNull(filter.getSearchName()))
            stringBuilder.append(" AND certificate.name LIKE '%").append(filter.getSearchName()).append("%'");

        if (!Objects.isNull(filter.getSearchDescription()))
            stringBuilder.append(" AND certificate.description LIKE '%").
                    append(filter.getSearchDescription()).append("%'");

        if (!Objects.isNull(filter.getSort()) && filter.getSort().equals("name"))
            stringBuilder.append(" ORDER BY ").append("certificate.name");

        if (!Objects.isNull(filter.getSort()) && filter.getSort().equals("createDate"))
            stringBuilder.append(" ORDER BY ").append("certificate.createDate");

        if (!Objects.isNull(filter.getSort()) && !Objects.isNull(filter.getOrder()) && filter.getOrder().equals("ASC"))
            stringBuilder.append(" ASC");

        if (!Objects.isNull(filter.getSort()) && !Objects.isNull(filter.getOrder()) && filter.getOrder().equals("DESC"))
            stringBuilder.append(" DESC");

        return String.valueOf(stringBuilder);
    }

}
