package com.epam.esm.service;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificate getGiftCertificate(String name) throws ServiceException;
    GiftCertificate getGiftCertificate(int id) throws ServiceException;
    List<GiftCertificate> getAllGiftCertificates() throws ServiceException;
    List<GiftCertificate> getAllGiftCertificates(String content) throws ServiceException;
    List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException;
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending) throws ServiceException;
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending) throws ServiceException;

    int addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;
    boolean deleteGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;
    boolean updateGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;
}
