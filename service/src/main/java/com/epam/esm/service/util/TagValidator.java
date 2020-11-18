package com.epam.esm.service.util;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

public class TagValidator {

    private TagValidator() {
    }

    public static void validateTag(Tag tag) throws ServiceException {
        if (tag == null) {
            throw new ServiceException("Failed to validate: tag is empty", ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
        validateId(tag.getId());
        validateName(tag.getName());
    }

    public static void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: tag id is negative",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }

    public static void validateName(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Failed to validate: tag name is empty",
                    ErrorCodeEnum.TAG_VALIDATION_ERROR);
        }
    }
}
