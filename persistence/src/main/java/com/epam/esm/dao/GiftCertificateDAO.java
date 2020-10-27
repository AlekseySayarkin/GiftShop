package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateDAO {

    GiftCertificate getGiftCertificate(String name);
    GiftCertificate getGiftCertificate(int id);
    List<GiftCertificate> getAllGiftCertificates();
    List<GiftCertificate> getAllGiftCertificates(String content);
    List<GiftCertificate> getGiftCertificateByTagName(String tagName);
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending);
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending);

    int addGiftCertificate(GiftCertificate giftCertificate);
    boolean deleteGiftCertificate(GiftCertificate giftCertificate);
    boolean updateGiftCertificate(GiftCertificate giftCertificate);
}
