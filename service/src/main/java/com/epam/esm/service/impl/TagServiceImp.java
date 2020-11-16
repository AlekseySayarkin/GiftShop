package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.TagValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;

public class TagServiceImp implements TagService {

    private static final Logger LOGGER = LogManager.getLogger(TagServiceImp.class);

    private final TagDao tagDao;

    @Autowired
    public TagServiceImp(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public Tag getTag(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Invalid name", new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return tagDao.getTag(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(String name): " + e.getMessage());
            throw new ServiceException("Failed to get tag",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG.getCode()));
        }
    }

    @Override
    public Tag getTag(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id", new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return tagDao.getTag(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG.getCode()));
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTags(): " + e.getMessage());
            throw new ServiceException("Failed to get tags",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG.getCode()));
        }
    }

    @Override
    public int addTag(Tag tag) throws ServiceException {
        TagValidator.validateTag(tag);
        try {
            return tagDao.addTag(tag);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException("Failed to add tag", new ErrorCode(ErrorCodeEnum.FAILED_TO_ADD_TAG.getCode()));
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException("Unable to ged tag id",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_ADD_TAG.getCode()));
        }
    }

    @Override
    public void deleteTag(int tagId) throws ServiceException {
        if (tagId <= 0) {
            throw new ServiceException("Invalid id",  new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            if (!tagDao.deleteTag(tagId)) {
                LOGGER.error("Failed to delete tag");
                throw new ServiceException("Failed to delete tag",
                        new ErrorCode(ErrorCodeEnum.FAILED_TO_DELETE_TAG.getCode() + tagId));
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_DELETE_TAG.getCode() + tagId));
        }
    }
}
