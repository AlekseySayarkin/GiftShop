package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.CertificateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDao tagDao;

    private static final int INVALID_INPUT = 40402;
    private static final int FAILED_TO_GET = 50102;
    private static final int FAILED_TO_ADD = 50202;
    private static final int FAILED_TO_DELETE = 503;
    private static final int FAILED_TO_UPDATE = 504;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDao tagDao) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate getGiftCertificate(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Invalid name", new ErrorCode(INVALID_INPUT));
        }

        try {
            return giftCertificateDAO.getGiftCertificate(name);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id",  new ErrorCode(INVALID_INPUT));
        }

        try {
            return giftCertificateDAO.getGiftCertificate(id);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificates();
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate",  new ErrorCode(INVALID_INPUT));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates(String content) throws ServiceException {
        if (content == null || content.isEmpty()) {
            throw new ServiceException("Invalid content",  new ErrorCode(INVALID_INPUT));
        }

        try {
            return giftCertificateDAO.getAllGiftCertificates(content);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException {
        if (tagName == null || tagName.isEmpty()) {
            throw new ServiceException("Invalid tag name",  new ErrorCode(INVALID_INPUT));
        }

        try {
            return giftCertificateDAO.getGiftCertificateByTagName(tagName);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByName(isAscending);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByDate(isAscending);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_GET));
        }
    }

    @Override
    @Transactional
    public int addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (CertificateValidator.isNonValid(giftCertificate)) {
            throw new ServiceException("Invalid certificate",  new ErrorCode(INVALID_INPUT));
        }

        try {
            giftCertificate.setId(giftCertificateDAO.addGiftCertificate(giftCertificate));

            addNewTagsToCertificate(giftCertificate);

            return giftCertificate.getId();
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate", new ErrorCode(FAILED_TO_ADD));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to return certificate id", new ErrorCode(FAILED_TO_ADD));
        }
    }

    @Override
    @Transactional
    public boolean deleteGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (CertificateValidator.isNonValid(giftCertificate)) {
            throw new ServiceException("Invalid certificate", new ErrorCode(INVALID_INPUT));
        }

        try {
            for (Tag tag : giftCertificate.getTags()) {
                giftCertificateDAO.deleteCertificateTagRelation(giftCertificate.getId(), tag.getId());
            }

            return giftCertificateDAO.deleteGiftCertificate(giftCertificate.getId());
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(FAILED_TO_DELETE + giftCertificate.getId()));
        }
    }

    @Override
    @Transactional
    public boolean updateGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        if (CertificateValidator.isNonValid(giftCertificate)) {
            throw new ServiceException("Invalid certificate",  new ErrorCode(INVALID_INPUT));
        }

        try {
            addNewTagsToCertificate(giftCertificate);

            return giftCertificateDAO.updateGiftCertificate(giftCertificate);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(FAILED_TO_UPDATE + giftCertificate.getId()));
        } catch (PersistenceException e) {
            throw new ServiceException("Failed to return certificate id",
                    new ErrorCode(FAILED_TO_UPDATE + giftCertificate.getId()));
        }
    }

    public void addNewTagsToCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        List<Tag> tagsInDataSource = tagDao.getAllTags();
        for (Tag tag : giftCertificate.getTags()) {
            if (!tagsInDataSource.contains(tag)) {
                tag.setId(tagDao.addTag(tag));
            }

            giftCertificateDAO.createCertificateTagRelation(giftCertificate.getId(), tag.getId());
        }
    }
}
