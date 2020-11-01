package com.epam.esm.repo.mapper;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CertificateRowMapper implements RowMapper<GiftCertificate> {

    private String CERTIFICATE_ID = "c_id";
    private String CERTIFICATE_NAME = "c_name";
    private String CERTIFICATE_DESCRIPTION = "c_description";
    private String CERTIFICATE_DURATION = "c_duration";
    private String CERTIFICATE_CREATE_DATE = "c_createDate";
    private String CERTIFICATE_LAST_UPDATE_DATE = "c_lastUpdateDate";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {

        return GiftCertificate.builder()
                .id(rs.getLong(CERTIFICATE_ID))
                .name(rs.getString(CERTIFICATE_NAME))
                .description(rs.getString(CERTIFICATE_DESCRIPTION))
                .duration(rs.getInt(CERTIFICATE_DURATION))
                .createDate(rs.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime())
                .lastUpdateDate(rs.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime())
                .build();
    }
}
