package com.epam.esm.service.util;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

public class TagValidator {

    private TagValidator() {
    }

    public static void validateTag(Tag tag) throws ServiceException {
        if (tag == null || tag.getId() < 0) {
            throw new ServiceException(new ErrorCode(ErrorCodeEnum.TAG_VALIDATION_ERROR.getCode()));
        }

        if (!isValidName(tag.getName())) {
            throw new ServiceException(new ErrorCode(ErrorCodeEnum.TAG_VALIDATION_ERROR.getCode()));
        }
    }

    private static boolean isValidName(String name) {
        return name != null && name.isEmpty();
    }
}
