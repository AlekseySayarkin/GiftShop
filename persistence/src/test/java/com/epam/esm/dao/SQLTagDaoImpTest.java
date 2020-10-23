package com.epam.esm.dao;

import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

public class SQLTagDaoImpTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Tag> mapper = new TagRowMapper();
    private TagDao tagDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        tagDao = new SQLTagDaoImpl(jdbcTemplate, mapper);
    }

    @Test
    public void whenMockJdbcTemplate_thenCorrectlyReturnAllTags() {
        String SQL_GET_ALL = "select Id, Name from Tags";

        List<Tag> tagsToReturn = new ArrayList<>();
        tagsToReturn.add(new Tag(1, "First tag"));
        tagsToReturn.add(new Tag(2, "Second tag"));
        tagsToReturn.add(new Tag(3, "Third tag"));

        Mockito.when(jdbcTemplate.query(SQL_GET_ALL, mapper)).thenReturn(tagsToReturn);

        Assert.assertEquals(tagsToReturn, tagDao.getAllTags());
    }

    @Test
    public void whenMockJdbcTemplate_thenCorrectlyDeleteTag() {
        TagDao tagDao = new SQLTagDaoImpl(jdbcTemplate, mapper);

        String SQL_DELETE_TAG = "delete from GiftShop.Tags where ID = ?";
        Tag existingTag = new Tag(1, "Existing tag");
        Tag notExistingTag = new Tag(2, "Not Existing Tag");

        Mockito.when(jdbcTemplate.update(SQL_DELETE_TAG, existingTag.getId())).thenReturn(1);

        Assert.assertTrue(tagDao.deleteTag(existingTag));
        Assert.assertFalse(tagDao.deleteTag(notExistingTag));
    }
}
