package com.epam.esm.service.util;

import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

import java.time.ZonedDateTime;

public class CertificateValidator {

    private CertificateValidator() {
    }

    public static void validateCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificate == null) {
            throw new ServiceException("Failed to validate: certificate is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }

        validateId(giftCertificate.getId());
        validateName(giftCertificate.getName());
        validateDescription(giftCertificate.getDescription());
        validatePrice(giftCertificate.getPrice());
        validateCreateAndUpdateDates(giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate());
        validateDuration(giftCertificate.getDuration());
    }

    public static void validateId(int id) throws ServiceException {
        if (id < 0) {
            throw new ServiceException("Failed to validate: certificate id is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    public static void validateName(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Failed to validate: certificate name is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    public static void validateDescription(String description) throws ServiceException {
        if (description == null || description.isEmpty()) {
            throw new ServiceException("Failed to validate: certificate description is empty",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private static void validatePrice(double price) throws ServiceException{
        if (price < 0) {
            throw new ServiceException("Failed to validate: certificate price is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private static void validateCreateAndUpdateDates(ZonedDateTime createDate, ZonedDateTime updateDate)
            throws ServiceException {
        if (createDate == null && updateDate == null) {
            return;
        }
        if (createDate == null) {
            throw new ServiceException("Failed to validate: certificate create date is null while update date isn't",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
        if (updateDate.isBefore(createDate)) {
            throw new ServiceException("Failed to validate: certificate update date is before create date",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }

    private static void validateDuration(int duration) throws ServiceException {
        if (duration < 0) {
            throw new ServiceException("Failed to validate: certificate price is negative",
                    ErrorCodeEnum.CERTIFICATE_VALIDATION_ERROR);
        }
    }
}
