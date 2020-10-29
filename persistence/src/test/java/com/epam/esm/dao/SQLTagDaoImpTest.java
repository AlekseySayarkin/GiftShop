package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
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
    public void whenAddTag_thenCorrectlyReturnIt() {
        try {
            Tag tagToReturn = new Tag("Tag to return");
            tagToReturn.setId(tagDao.addTag(tagToReturn));

            Assert.assertEquals(tagToReturn, tagDao.getTagById(tagToReturn.getId()));
            Assert.assertEquals(tagToReturn, tagDao.getTagByName(tagToReturn.getName()));
        } catch (PersistenceException e) {
            Assert.fail();
        }
    }

    @Test
    public void whenAddTag_thenCorrectlyDeleteId() {
        try {
            Tag tagToDelete = new Tag("Tag to return");
            tagToDelete.setId(tagDao.addTag(tagToDelete));
            Assert.assertTrue(tagDao.deleteTag(tagToDelete));
        } catch (PersistenceException e) {
            Assert.fail();
        }
    }

    @Test
    public void whenAddTag_thenReturnNonZeroId() {
        try {
            Tag tagToAdd = new Tag("Tag to add");
            Assert.assertNotEquals(0, tagDao.addTag(tagToAdd));
        } catch (PersistenceException e) {
            Assert.fail();
        }
    }

    @Test
    public void whenAddTags_thenCorrectlyReturnsIt() {
        try {
            List<Tag> tagsToReturn = new ArrayList<>();
            tagsToReturn.add(new Tag("Tag one"));
            tagsToReturn.add(new Tag("Tag two"));
            tagsToReturn.add(new Tag("Tag three"));

            for (Tag tag: tagsToReturn) {
                tag.setId(tagDao.addTag(tag));
            }

            Assert.assertEquals(tagsToReturn, tagDao.getAllTags());
        } catch (PersistenceException e) {
            Assert.fail();
        }
    }
}
