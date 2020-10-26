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
    public void whenAddTag_thenCorrectlyReturnIt() throws PersistenceException {
        Tag actual;
        Tag expected;

        actual = new Tag("Tag to return");
        actual.setId(tagDao.addTag(actual));

        expected = tagDao.getTag(actual.getId());
        Assert.assertEquals(actual, expected);

        expected = tagDao.getTag(actual.getName());
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void whenAddTag_thenCorrectlyDeleteId() throws PersistenceException {
        Tag tag = new Tag("Tag to delete");
        tag.setId(tagDao.addTag(tag));

        boolean result = tagDao.deleteTag(tag.getId());
        Assert.assertTrue(result);
    }

    @Test
    public void whenAddTag_thenReturnNonZeroId() throws PersistenceException {
        Tag tag = new Tag("Tag to add");

        int id = tagDao.addTag(tag);
        Assert.assertNotEquals(0, id);
    }

    @Test
    public void whenAddTags_thenCorrectlyReturnsIt() throws PersistenceException {
        List<Tag> expected;
        List<Tag> actual;

        expected = new ArrayList<>();
        expected.add(new Tag("Tag one"));
        expected.add(new Tag("Tag two"));
        expected.add(new Tag("Tag three"));

        for (Tag tag: expected) {
            tag.setId(tagDao.addTag(tag));
        }

        actual = tagDao.getAllTags();
        Assert.assertEquals(expected, actual);
    }
}
