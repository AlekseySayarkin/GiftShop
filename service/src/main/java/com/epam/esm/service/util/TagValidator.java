package com.epam.esm.service.util;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

public class TagValidator {

    private static final int TAG_VALIDATION_ERROR_CODE = 50502;

    private TagValidator() {
    }

    public static void validateTag(Tag tag) throws ServiceException {
        if (tag == null || tag.getId() < 0) {
            throw new ServiceException(new ErrorCode(TAG_VALIDATION_ERROR_CODE));
        }

        if (!isValidName(tag.getName())) {
            throw new ServiceException(new ErrorCode(TAG_VALIDATION_ERROR_CODE));
        }
    }

    private static boolean isValidName(String name) {
        return name != null && name.isEmpty();
    }
}
