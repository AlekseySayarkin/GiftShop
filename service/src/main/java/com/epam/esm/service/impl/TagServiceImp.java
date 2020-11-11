package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class TagServiceImp implements TagService {

    private final TagDao tagDao;

    private static final int INVALID_INPUT = 40401;
    private static final int FAILED_TO_GET = 50101;
    private static final int FAILED_TO_ADD = 50201;
    private static final int FAILED_TO_DELETE = 503;

    @Autowired
    public TagServiceImp(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getTag(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Invalid name", new ErrorCode(INVALID_INPUT));
        }

        try {
            return tagDao.getTag(name);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public Tag getTag(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id", new ErrorCode(INVALID_INPUT));
        }

        try {
            return tagDao.getTag(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tag", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get tags", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public int addTag(Tag tag) throws ServiceException {
        if (!TagValidator.isValid(tag)) {
            throw new ServiceException("Invalid tag", new ErrorCode(INVALID_INPUT));
        }

        try {
            return tagDao.addTag(tag);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to add tag", new ErrorCode(FAILED_TO_ADD));
        } catch (PersistenceException e) {
            throw new ServiceException("Unable to ged tag id", new ErrorCode(FAILED_TO_ADD));
        }
    }

    @Override
    public boolean deleteTag(int tagId) throws ServiceException {
        if (tagId <= 0) {
            throw new ServiceException("Invalid id", new ErrorCode(INVALID_INPUT));
        }

        try {
            return tagDao.deleteTag(tagId);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to delete tag", new ErrorCode(FAILED_TO_DELETE + tagId));
        }
    }
}
