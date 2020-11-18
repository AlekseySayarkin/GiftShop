package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
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
        TagValidator.validateName(name);
        try {
            return tagDao.getTag(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(String name): " + e.getMessage());
            throw new ServiceException("Failed to get tag by its name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag getTag(int id) throws ServiceException {
        TagValidator.validateId(id);
        try {
            return tagDao.getTag(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getTag(int id): " + e.getMessage());
            throw new ServiceException("Failed to get tag by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public List<Tag> getAllTags() throws ServiceException {
        try {
            return tagDao.getAllTags();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllTags(): " + e.getMessage());
            throw new ServiceException("Failed to get all tags", ErrorCodeEnum.FAILED_TO_RETRIEVE_TAG);
        }
    }

    @Override
    public Tag addTag(Tag tag) throws ServiceException {
        TagValidator.validateTag(tag);
        try {
            int id = tagDao.addTag(tag);
            tag.setId(id);
            return tag;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException("Failed to add tag: it already exist", ErrorCodeEnum.FAILED_TO_ADD_TAG);
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown in addTag(): " + e.getMessage());
            throw new ServiceException(e.getMessage(), ErrorCodeEnum.FAILED_TO_ADD_TAG);
        }
    }

    @Override
    public void deleteTag(int tagId) throws ServiceException {
        TagValidator.validateId(tagId);
        try {
            if (!tagDao.deleteTag(tagId)) {
                LOGGER.error("Failed to delete tag");
                throw new ServiceException("Failed to delete tag because it id (" + tagId + ") is not found",
                        ErrorCodeEnum.FAILED_TO_DELETE_TAG);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteTag(): " + e.getMessage());
            throw new ServiceException("Failed to delete tag by it id: " + tagId,
                    ErrorCodeEnum.FAILED_TO_DELETE_TAG);
        }
    }
}
