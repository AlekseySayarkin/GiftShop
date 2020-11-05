package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TagServiceImpTest {

    @Mock
    private TagDao tagDao;

    private TagService tagService;

    @Before
    public void init() {
        tagDao = Mockito.mock(SQLTagDaoImpl.class);
        tagService = new TagServiceImp(tagDao);
    }

    @Test
    public void whenGetTag_thenCorrectlyReturnsIt() throws ServiceException {
        Tag expected;
        Tag actual;

        expected = new Tag(1, "spa");

        Mockito.when(tagDao.getTag(expected.getId())).thenReturn(expected);
        Mockito.when(tagDao.getTag(expected.getName())).thenReturn(expected);

        actual = tagService.getTag(expected.getId());
        Assert.assertEquals(expected, actual);

        actual = tagService.getTag(expected.getName());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenReturnAllTags_thenCorrectlyReturnAllTags() throws ServiceException {
        List<Tag> expected;
        List<Tag> actual;

        expected = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            expected.add(new Tag(i, "Tag " + i));
        }

        Mockito.when(tagDao.getAllTags()).thenReturn(expected);
        actual = tagService.getAllTags();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenAddTag_thenReturnItId() throws PersistenceException, ServiceException {
        Tag tag = new Tag("spa");
        int expectedId = 1;

        Mockito.when(tagDao.addTag(tag)).thenReturn(expectedId);
        tag.setId(tagService.addTag(tag));

        Assert.assertEquals(expectedId, tag.getId());
    }

    @Test
    public void whenTryAddVoidTag_thenThrowException() {
        Tag tag = new Tag();

        try {
            tag.setId(tagService.addTag(tag));
        } catch (ServiceException e) {
            Assert.assertEquals("Invalid tag", e.getMessage());
        }
    }

    @Test
    public void whenDeleteTag_thenReturnTrue() throws ServiceException {
        Tag tag = new Tag(1, "spa");

        Mockito.when(tagDao.deleteTag(tag.getId())).thenReturn(true);
        boolean result = tagService.deleteTag(tag.getId());

        Assert.assertTrue(result);
    }
}
