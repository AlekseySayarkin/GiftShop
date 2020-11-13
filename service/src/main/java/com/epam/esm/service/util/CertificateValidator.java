package com.epam.esm.service.util;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

import java.time.ZonedDateTime;

public class CertificateValidator {

    private static final int GIFT_CERTIFICATE_VALIDATION_ERROR_CODE = 50501;

    private CertificateValidator() {
    }

    public static void validateCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificate == null || giftCertificate.getId() < 0) {
            throw new ServiceException(new ErrorCode(GIFT_CERTIFICATE_VALIDATION_ERROR_CODE));
        }
        if (!isValidName(giftCertificate.getName()) || !isValidDescription(giftCertificate.getDescription())) {
            throw new ServiceException(new ErrorCode(GIFT_CERTIFICATE_VALIDATION_ERROR_CODE));
        }
        if (!isValidPrice(giftCertificate.getPrice())) {
            throw new ServiceException(new ErrorCode(GIFT_CERTIFICATE_VALIDATION_ERROR_CODE));
        }
        if (!isValidCreateAndUpdateDates(giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate())) {
            throw new ServiceException(new ErrorCode(GIFT_CERTIFICATE_VALIDATION_ERROR_CODE));
        }
        if(!isValidDuration(giftCertificate.getDuration())) {
            throw new ServiceException(new ErrorCode(GIFT_CERTIFICATE_VALIDATION_ERROR_CODE));
        }
    }

    private static boolean isValidName(String name) {
        return name != null && !name.isEmpty();
    }

    private static boolean isValidDescription(String description) {
        return description != null && !description.isEmpty();
    }

    private static boolean isValidPrice(double price) {
        return price < 0;
    }

    private static boolean isValidCreateAndUpdateDates(ZonedDateTime createDate, ZonedDateTime updateDate) {
        if (createDate == null) {
            return false;
        }

        if (updateDate != null) {
            return createDate.isBefore(updateDate);
        }

        return true;
    }

    private static boolean isValidDuration(int duration) {
        return duration < 0;
    }
}
