package com.epam.esm.service;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.TagServiceImp;
import com.epam.esm.service.util.impl.TagValidatorImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class TagServiceImpTest {

    private TagDao tagDao;
    private TagService tagService;

    @Before
    public void init() {
        tagDao = Mockito.mock(SQLTagDaoImpl.class);
        tagService = new TagServiceImp(tagDao, new TagValidatorImpl());
    }

    @Test
    public void whenGetTag_thenCorrectlyReturnItById() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getTag(given.getId())).thenReturn(given);

        Tag actual = tagService.getTag(given.getId());
        Assert.assertEquals(given, actual);
        Mockito.verify(tagDao).getTag(given.getId());
    }

    @Test
    public void whenGetTag_thenCorrectlyReturnItByName() throws ServiceException {
        Tag given = new Tag(1, "spa");

        Mockito.when(tagDao.getTag(given.getName())).thenReturn(given);

        Tag actual = tagService.getTag(given.getName());
        Assert.assertEquals(given, actual);
        Mockito.verify(tagDao).getTag(given.getName());
    }


    @Test
    public void whenAddTags_thenCorrectlyReturnThem() throws ServiceException {
        List<Tag> expected = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            expected.add(new Tag(i, "Tag " + i));
        }

        Mockito.when(tagDao.getAllTags()).thenReturn(expected);

        List<Tag> actual = tagService.getAllTags();
        Assert.assertEquals(expected, actual);
        Mockito.verify(tagDao).getAllTags();
    }

    @Test
    public void whenTryAddVoidTag_thenThrowException() {
        Tag tag = new Tag();

        try {
            tagService.addTag(tag);
        } catch (ServiceException e) {
            Assert.assertEquals("Failed to validate: tag name is empty", e.getMessage());
        }
    }

    @Test
    public void whenTryDeleteNonExistingTag_thenThrowException() {
        Tag tag = new Tag(1, "spa");

        try {
            tagService.deleteTag(tag.getId());
        } catch (ServiceException e) {
            Assert.assertEquals("Failed to delete tag because it id ("
                    + tag.getId() +") is not found", e.getMessage());
        }
        Mockito.verify(tagDao).deleteTag(tag.getId());
    }
}
