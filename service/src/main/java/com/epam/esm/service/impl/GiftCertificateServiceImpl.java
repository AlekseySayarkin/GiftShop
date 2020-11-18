package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.CertificateRequestBody;
import com.epam.esm.dao.exception.ErrorCodeEnum;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.util.CertificateValidator;
import org.apache.commons.lang3.EnumUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class GiftCertificateServiceImpl implements GiftCertificateService {

    public enum SortParameter {
        DATE, NAME
    }

    public enum SortType {
        ASC, DESC
    }

    private static final Logger LOGGER = LogManager.getLogger(GiftCertificateServiceImpl.class);

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagDao tagDao;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDAO giftCertificateDAO, TagDao tagDao) {
        this.giftCertificateDAO = giftCertificateDAO;
        this.tagDao = tagDao;
    }

    @Override
    public GiftCertificate getGiftCertificate(String name) throws ServiceException {
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Invalid name", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getGiftCertificate(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getGiftCertificate(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByContent() throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificates();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByContent(String content) throws ServiceException {
        if (content == null || content.isEmpty()) {
            throw new ServiceException("Invalid content", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getAllGiftCertificates(content);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(String content): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException {
        if (tagName == null || tagName.isEmpty()) {
            throw new ServiceException("Invalid tag name", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getGiftCertificateByTagName(tagName);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificateByTagName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByName(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByDate(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByDate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }
    @Override
    public List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws ServiceException {
        if (requestBody == null) {
            return getGiftCertificatesByContent();
        }

        if (requestBody.getContent() != null) {
            return getGiftCertificatesByContent(requestBody.getContent());
        }
        if (requestBody.getSort() != null) {
            return getSortedCertificates(requestBody.getSort());
        }
        if (requestBody.getTagName() != null) {
            return getGiftCertificateByTagName(requestBody.getTagName());
        }
        throw new ServiceException("Wrong request input", ErrorCodeEnum.INVALID_SORT_INPUT);
    }

    private List<GiftCertificate> getSortedCertificates(String sort) throws ServiceException {
        int separator = sort.indexOf(".");
        if (separator != -1 && separator < sort.length()) {
            String sortBy = sort.substring(0 , separator).toUpperCase();
            String sortType = sort.substring(++separator).toUpperCase();

            if (!EnumUtils.isValidEnum(SortParameter.class, sortBy)) {
                throw new ServiceException("Wrong sort input", ErrorCodeEnum.INVALID_SORT_INPUT);
            }

            return switch (SortParameter.valueOf(sortBy)) {
                case DATE -> getAllGiftCertificatesSortedByDate(isAscending(sortType));
                case NAME -> getAllGiftCertificatesSortedByName(isAscending(sortType));
            };
        } else {
            throw new ServiceException("Wrong sort input", ErrorCodeEnum.INVALID_SORT_INPUT);
        }
    }

    private boolean isAscending(String sort) throws ServiceException {
        if (!EnumUtils.isValidEnum(SortType.class, sort)) {
            throw new ServiceException("Wrong sort input", ErrorCodeEnum.INVALID_SORT_INPUT);
        }

        return switch (SortType.valueOf(sort)) {
            case ASC -> true;
            case DESC -> false;
        };
    }

    @Override
    @Transactional
    public GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        CertificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            giftCertificate.setLastUpdateDate(giftCertificate.getCreateDate());

            int id = giftCertificateDAO.addGiftCertificate(giftCertificate);
            giftCertificate.setId(id);

            addNewTagsToCertificate(giftCertificate);

            return giftCertificate;
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown: " + e.getMessage());
            throw new ServiceException("Failed to return certificate id", ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public void deleteGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        CertificateValidator.validateCertificate(giftCertificate);
        try {
            for (Tag tag : giftCertificate.getTags()) {
                giftCertificateDAO.deleteCertificateTagRelation(giftCertificate.getId(), tag.getId());
            }

            if (!giftCertificateDAO.deleteGiftCertificate(giftCertificate.getId())) {
                LOGGER.error("Failed to delete certificate");
                throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
        }
    }

    @Override
    @Transactional
    public void updateGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        CertificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setLastUpdateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));

            addNewTagsToCertificate(giftCertificate);

            if (!giftCertificateDAO.updateGiftCertificate(giftCertificate)) {
                LOGGER.error("Failed to update certificate");
                throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
            }
        } catch (DataAccessException | PersistenceException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to update certificate", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
        }
    }

    public void addNewTagsToCertificate(GiftCertificate giftCertificate) throws PersistenceException {
        List<Tag> tagsInDataSource = tagDao.getAllTags();
        for (Tag tag : giftCertificate.getTags()) {
            if (!tagsInDataSource.contains(tag)) {
                tag.setId(tagDao.addTag(tag));
            }

            try {
                giftCertificateDAO.createCertificateTagRelation(giftCertificate.getId(), tag.getId());
            } catch (DataAccessException ignored) {
            }
        }
    }
}
