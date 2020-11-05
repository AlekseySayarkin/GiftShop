package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.Validator;
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
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Invalid name", ErrorCode.NOT_FOUND);
        }

        try {
            return tagDao.getTag(name);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag", ErrorCode.NOT_FOUND);
        }
    }

    @Override
    public Tag getTag(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id", ErrorCode.NOT_FOUND);
        }

        try {
            return tagDao.getTag(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag", ErrorCode.NOT_FOUND);
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tags", ErrorCode.NOT_FOUND);
        }
    }

    @Override
    public int addTag(Tag tag) throws ServiceException {
        if (!Validator.isValid(tag)) {
            throw new ServiceException("Invalid tag", ErrorCode.NOT_FOUND);
        }

        try {
            return tagDao.addTag(tag);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to add tag", ErrorCode.FAILED_TO_ADD);
        } catch (PersistenceException e) {
            throw new ServiceException("Unable to ged tag id", ErrorCode.FAILED_TO_ADD);
        }
    }

    @Override
    public boolean deleteTag(int tagId) throws ServiceException {
        if (tagId <= 0) {
            throw new ServiceException("Invalid id", ErrorCode.NOT_FOUND);
        }

        try {
            return tagDao.deleteTag(tagId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to delete tag", ErrorCode.FAILED_TO_DELETE);
        }
    }
}
