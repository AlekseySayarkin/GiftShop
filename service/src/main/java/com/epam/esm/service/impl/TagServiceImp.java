package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class TagServiceImp implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImp(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getTag(String name) throws ServiceException {
        try {
            return tagDao.getTagByName(name);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag");
        }
    }

    @Override
    public Tag getTag(int id) throws ServiceException {
        try {
            return tagDao.getTagById(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag");
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tags");
        }
    }

    @Override
    public int addTag(Tag tag) throws ServiceException {
        try {
            return tagDao.addTag(tag);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to add tag");
        } catch (PersistenceException e) {
            throw new ServiceException("Unable to ged tag id");
        }
    }

    @Override
    public boolean deleteTag(int tagId) throws ServiceException {
        try {
            return tagDao.deleteTag(new Tag(tagId, "replace"));
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to delete tag");
        }
    }
}
