package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SQLTagDaoImpl implements TagDao {

    private static final String SQL_GET_ALL = "select Id, Name from Tags";
    private static final String SQL_GET_BY_NAME = "select Id, Name from Tags where Name = ?";
    private static final String SQL_GET_BY_ID = "select Id, Name from Tags where Id = ?";
    private static final String SQL_ADD_TAG = "INSERT INTO GiftShop.Tags (Name) VALUES (?)";
    private static final String SQL_DELETE_TAG = "DELETE FROM GiftShop.Tags WHERE ID = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    @Autowired
    public SQLTagDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = rowMapper;
    }

    @Override
    public Tag getTagByName(String name) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_NAME, new Object[] { name }, tagRowMapper);
    }

    @Override
    public Tag getTagById(int id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] { id }, tagRowMapper);
    }

    @Override
    public List<Tag> getAllTags() {
        return jdbcTemplate.query(SQL_GET_ALL, tagRowMapper);
    }

    @Override
    public int addTag(Tag tag) {
        //todo add correct implementation
        return jdbcTemplate.queryForObject(SQL_ADD_TAG, Tag.class ,tag.getName()).getId();
    }

    @Override
    public boolean deleteTag(Tag tag) {
        return jdbcTemplate.update(SQL_DELETE_TAG, tag.getId()) == 1;
    }
}
