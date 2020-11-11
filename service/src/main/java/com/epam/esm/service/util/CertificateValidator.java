package com.epam.esm.service.util;

import com.epam.esm.model.GiftCertificate;

import java.time.ZonedDateTime;

public class CertificateValidator {

    private CertificateValidator() {
    }

    public static boolean isNonValid(GiftCertificate giftCertificate) {
        if (giftCertificate == null || giftCertificate.getId() < 0) {
            return true;
        }

        return !isValidNameAndDescription(giftCertificate.getName(), giftCertificate.getDescription()) &&
                !isValidPrice(giftCertificate.getPrice()) &&
                !isValidCreateAndUpdateDates(
                        giftCertificate.getCreateDate(), giftCertificate.getLastUpdateDate()) &&
                !isValidDuration(giftCertificate.getDuration());
    }

    private static boolean isValidNameAndDescription(String name, String description) {
        return name != null && !name.isEmpty() && description != null && !description.isEmpty();
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
