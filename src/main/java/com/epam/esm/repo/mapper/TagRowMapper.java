package com.epam.esm.repo.mapper;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper<Tag> {

    private String TAG_ID = "t_id";
    private String TAG_NAME = "t_name";

    @Override
    public Tag mapRow(ResultSet rs, int i) throws SQLException {
        return Tag.builder().id(rs.getLong(TAG_ID)).name(rs.getString(TAG_NAME)).build();
    }
}
