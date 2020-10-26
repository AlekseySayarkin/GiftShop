package com.epam.esm.dao;

import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class SQLTagDaoImpTest {

    private TagDao tagDao;

    @Before
    public void init() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts("db/ClearTables.sql")
                .addScript("db/Tags.sql")
                .build();

        tagDao = new SQLTagDaoImpl(new JdbcTemplate(dataSource), new TagRowMapper());
    }

    @Test
    public void whenAddTag_thenCorrectlyDeleteId() {
        Tag tagToDelete = new Tag("Tag to delete");
        tagToDelete.setId(tagDao.addTag(tagToDelete));
        Assert.assertTrue(tagDao.deleteTag(tagToDelete.getId()));
    }

    @Test
    public void whenAddTag_thenCorrectlyReturnItById() {
        Tag tagToAdd = new Tag("Tag to add");
        tagToAdd.setId(tagDao.addTag(tagToAdd));

        Assert.assertEquals(tagToAdd, tagDao.getTag(tagToAdd.getId()));
    }

    @Test
    public void whenAddTag_thenCorrectlyReturnItByName() {
        Tag tagToReturn = new Tag("Tag to return");
        tagToReturn.setId(tagDao.addTag(tagToReturn));

        Assert.assertEquals(tagToReturn, tagDao.getTag(tagToReturn.getName()));
    }

    @Test
    public void whenAddTags_thenCorrectlyReturnsIt() {
        List<Tag> tagsToReturn = new ArrayList<>();
        tagsToReturn.add(new Tag("Tag one"));
        tagsToReturn.add(new Tag("Tag two"));
        tagsToReturn.add(new Tag("Tag three"));

        for (Tag tag: tagsToReturn) {
            tag.setId(tagDao.addTag(tag));
        }

        Assert.assertEquals(tagsToReturn, tagDao.getAllTags());
    }
}
