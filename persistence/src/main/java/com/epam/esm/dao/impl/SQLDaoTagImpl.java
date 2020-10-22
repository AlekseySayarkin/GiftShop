package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Qualifier("tagDao")
public class SQLDaoTagImpl implements TagDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        return jdbcTemplate.query("select * from Tags", (rs, rowNum) -> {
            Tag tag = new Tag();
            tag.setId(rs.getInt(1));
            tag.setName(rs.getString(2));
            return tag;
        });
    }

    @Override
    public boolean addTag(Tag tag) {
        return false;
    }

    @Override
    public boolean deleteTag(Tag tag) {
        return false;
    }
}
