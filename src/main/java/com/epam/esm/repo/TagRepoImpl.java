package com.epam.esm.repo;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.repo.mapper.TagRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TagRepoImpl implements TagRepo {

    private static final String GET_BY_ID = "SELECT tag.id t_id, tag.name t_name FROM `tag` WHERE tag.id=?";
    private static final String GET_ALL = "SELECT tag.id t_id, tag.name t_name FROM `tag`";
    private static final String INSERT_TAG = "INSERT INTO `tag` (`name`) VALUES (?)";
    private static final String DELETE_TAG = "DELETE FROM `tag` WHERE id=?";
    private static final String GET_BY_NAME = "SELECT tag.id t_id, tag.name t_name FROM `tag` WHERE tag.name=?";

    private final JdbcTemplate jdbcTemplate;
    private final TagRowMapper tagRowMapper;

    @Override
    public Tag findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new Object[]{id}, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Resource not found", e, id);
        }
    }

    @Override
    public Tag findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_NAME, new Object[]{name}, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Resource not found", e);
        }
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, tagRowMapper);
    }

    @Override
    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_TAG, new String[]{"id"});
                    ps.setString(1, tag.getName());
                    return ps;
                },
                keyHolder);
        return (long) keyHolder.getKey();
    }

    @Override
    public void delete(Long id) {
        this.jdbcTemplate.update(DELETE_TAG, id);
    }
}
