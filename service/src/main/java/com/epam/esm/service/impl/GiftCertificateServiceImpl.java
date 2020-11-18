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
import com.epam.esm.service.util.TagValidator;
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
        CertificateValidator.validateName(name);
        try {
            return giftCertificateDAO.getGiftCertificate(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it name: " + name,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) throws ServiceException {
        CertificateValidator.validateId(id);
        try {
            return giftCertificateDAO.getGiftCertificate(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it id: " + id,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> geAllCertificatesByContent() throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificates();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(): " + e.getMessage());
            throw new ServiceException("Failed to get all certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificatesByContent(String content) throws ServiceException {
        if (content == null || content.isEmpty()) {
            throw new ServiceException("Failed to validate: content is empty", ErrorCodeEnum.INVALID_INPUT);
        }

        try {
            return giftCertificateDAO.getAllGiftCertificates(content);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(String content): " + e.getMessage());
            throw new ServiceException("Failed to get certificate by it content: " + content,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException {
        TagValidator.validateName(tagName);
        try {
            return giftCertificateDAO.getGiftCertificateByTagName(tagName);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificateByTagName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate Failed to get certificate by tag name: " + tagName,
                    ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByName(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificates", ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE);
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
            return geAllCertificatesByContent();
        }

        if (requestBody.getContent() != null) {
            return getGiftCertificatesByContent(requestBody.getContent());
        }
        if (requestBody.getSortType() != null) {
            if (requestBody.getSortBy() == null) {
                throw new ServiceException("Error: sort by input is missing", ErrorCodeEnum.INVALID_SORT_INPUT);
            }
            return getSortedCertificates(
                    requestBody.getSortType().toUpperCase(), requestBody.getSortBy().toUpperCase());
        } else {
            if (requestBody.getSortBy() != null) {
                throw new ServiceException("Error: sort type input is missing", ErrorCodeEnum.INVALID_SORT_INPUT);
            }
        }
        if (requestBody.getTagName() != null) {
            return getGiftCertificateByTagName(requestBody.getTagName());
        }
        throw new ServiceException("Error: request input body is empty", ErrorCodeEnum.INVALID_SORT_INPUT);
    }

    private List<GiftCertificate> getSortedCertificates(String sortType, String sortBy) throws ServiceException {
        if (!EnumUtils.isValidEnum(SortParameter.class, sortBy)) {
            throw new ServiceException("Error: wrong sort input: " +sortBy, ErrorCodeEnum.INVALID_SORT_INPUT);
        }

        return switch (SortParameter.valueOf(sortBy)) {
            case DATE -> getAllGiftCertificatesSortedByDate(isAscending(sortType));
            case NAME -> getAllGiftCertificatesSortedByName(isAscending(sortType));
        };
    }

    private boolean isAscending(String sort) throws ServiceException {
        if (!EnumUtils.isValidEnum(SortType.class, sort)) {
            throw new ServiceException("Error: wrong sort input: " + sort, ErrorCodeEnum.INVALID_SORT_INPUT);
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
            throw new ServiceException("Failed to add certificate: certificate already exist",
                    ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown: " + e.getMessage());
            throw new ServiceException(e.getMessage(), ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE);
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
                throw new ServiceException("Failed to delete certificate because it id "
                        + giftCertificate.getId() + ") is not found", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to delete certificate", ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE);
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
                throw new ServiceException("Failed to update certificate because it id "
                        + giftCertificate.getId() + ") is not found", ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE);
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
