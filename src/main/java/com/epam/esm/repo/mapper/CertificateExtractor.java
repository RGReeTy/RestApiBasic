package com.epam.esm.repo.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class CertificateExtractor implements ResultSetExtractor<Map<GiftCertificate, List<Tag>>> {

    private String CERTIFICATE_ID = "c_id";
    private String CERTIFICATE_NAME = "c_name";
    private String CERTIFICATE_DESCRIPTION = "c_description";
    private String CERTIFICATE_DURATION = "c_duration";
    private String CERTIFICATE_CREATE_DATE = "c_createDate";
    private String CERTIFICATE_LAST_UPDATE_DATE = "c_lastUpdateDate";
    private String TAG_ID = "t_id";
    private String TAG_NAME = "t_name";

    @Override
    public Map<GiftCertificate, List<Tag>> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        Map<GiftCertificate, List<Tag>> data = new LinkedHashMap<>();

        while (resultSet.next()) {
            GiftCertificate certificate = GiftCertificate.builder()
                    .id(resultSet.getLong(CERTIFICATE_ID))
                    .name(resultSet.getString(CERTIFICATE_NAME))
                    .description(resultSet.getString(CERTIFICATE_DESCRIPTION))
                    .duration(resultSet.getInt(CERTIFICATE_DURATION))
                    .createDate(resultSet.getTimestamp(CERTIFICATE_CREATE_DATE).toLocalDateTime())
                    .lastUpdateDate(resultSet.getTimestamp(CERTIFICATE_LAST_UPDATE_DATE).toLocalDateTime())
                    .build();

            data.putIfAbsent(certificate, new ArrayList<>());

            long tagId = resultSet.getLong(TAG_ID);

            if (tagId != 0) {
                Tag tag = Tag.builder()
                        .id(tagId)
                        .name(resultSet.getString(TAG_NAME))
                        .build();
                data.get(certificate).add(tag);
            }
        }

        return data;
    }
}
