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
    public void whenAddTag_thenCorrectlyReturnItById() throws PersistenceException {
        Tag given = new Tag("Tag to return by id");

        given.setId(tagDao.addTag(given));

        Tag actual = tagDao.getTag(given.getId());
        Assert.assertEquals(given, actual);
    }

    @Test
    public void whenAddTag_thenCorrectlyReturnItByName() throws PersistenceException {
        Tag given = new Tag("Tag to return by name");

        given.setId(tagDao.addTag(given));

        Tag actual = tagDao.getTag(given.getName());
        Assert.assertEquals(given, actual);
    }

    @Test
    public void whenAddTag_thenCorrectlyDeleteIdById() throws PersistenceException {
        Tag given = new Tag("Tag to delete");

        given.setId(tagDao.addTag(given));

        boolean actual = tagDao.deleteTag(given.getId());
        Assert.assertTrue(actual);
    }

    @Test
    public void whenAddTag_thenReturnNonZeroId() throws PersistenceException {
        Tag tag = new Tag("Tag to add");

        int id = tagDao.addTag(tag);

        Assert.assertNotEquals(0, id);
    }

    @Test
    public void whenAddTags_thenCorrectlyReturnsIt() throws PersistenceException {
        List<Tag> given = new ArrayList<>();
        given.add(new Tag("First tag"));
        given.add(new Tag("Second tag"));
        given.add(new Tag("Third tag"));

        for (Tag tag: given) {
            tag.setId(tagDao.addTag(tag));
        }

        List<Tag> actual = tagDao.getAllTags();
        Assert.assertEquals(given, actual);
    }
}
