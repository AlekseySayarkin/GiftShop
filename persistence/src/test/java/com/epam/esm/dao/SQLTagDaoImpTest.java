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
                .addScript("db/schema.sql")
                .build();


        tagDao = new SQLTagDaoImpl(new JdbcTemplate(dataSource), new TagRowMapper());
    }

    @Test
    public void whenAddTag_thenCorrectlyDeleteId() {
        Tag tagToDelete = new Tag(1, "Tag to return");
        tagDao.addTag(tagToDelete);
        Assert.assertTrue(tagDao.deleteTag(tagToDelete));
    }

    @Test
    public void whenAddTag_thenReturnNonZeroId() {
        Tag tagToAdd = new Tag(1, "Tag to add");
        Assert.assertNotEquals(0, tagDao.addTag(tagToAdd));

    }

    @Test
    public void whenAddTag_thenCorrectlyReturnIt() {
        Tag tagToReturn = new Tag(1, "Tag to return");
        tagDao.addTag(tagToReturn);
        Assert.assertEquals(tagToReturn, tagDao.getTagById(tagToReturn.getId()));
        Assert.assertEquals(tagToReturn, tagDao.getTagByName(tagToReturn.getName()));
    }

    @Test
    public void whenAddTags_thenCorrectlyReturnsIt() {
        List<Tag> tagsToReturn = new ArrayList<>();
        tagsToReturn.add(new Tag(1, "Tag one"));
        tagsToReturn.add(new Tag(2, "Tag two"));
        tagsToReturn.add(new Tag(3, "Tag three"));

        for (Tag tag: tagsToReturn) {
            tagDao.addTag(tag);
        }

        Assert.assertEquals(tagsToReturn, tagDao.getAllTags());
    }
}
