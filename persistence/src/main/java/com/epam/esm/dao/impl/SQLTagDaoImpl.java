package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.mapper.TagRawMapper;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SQLTagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SQLTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Tag getTagByName(String name) {
        return null;
    }

    @Override
    public Tag getTagById(int id) {
        return null;
    }

    @Override
    public List<Tag> getTags() {
        return jdbcTemplate.query("select * from Tags", new TagRawMapper());
    }

    @Override
    public int addTag(Tag tag) {
        return 0;
    }

    @Override
    public boolean deleteTag(Tag tag) {
        return false;
    }

    @Override
    public boolean updateTag(Tag tag) {
        return false;
    }
}
