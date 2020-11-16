package com.epam.esm.service.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.ErrorCode;
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

import java.util.List;

public class GiftCertificateServiceImpl implements GiftCertificateService {

    private enum SortParameter {
        DATE(),
        NAME();

        SortParameter(){}
    }

    private enum SortType {
        ASC(),
        DESC();

        SortType(){}
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
            throw new ServiceException("Invalid name", new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return giftCertificateDAO.getGiftCertificate(name);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(String name): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) throws ServiceException {
        if (id <= 0) {
            throw new ServiceException("Invalid id",  new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return giftCertificateDAO.getGiftCertificate(id);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificate(int id): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificates();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates(String content) throws ServiceException {
        if (content == null || content.isEmpty()) {
            throw new ServiceException("Invalid content",  new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return giftCertificateDAO.getAllGiftCertificates(content);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificates(String content): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException {
        if (tagName == null || tagName.isEmpty()) {
            throw new ServiceException("Invalid tag name",  new ErrorCode(ErrorCodeEnum.INVALID_INPUT.getCode()));
        }

        try {
            return giftCertificateDAO.getGiftCertificateByTagName(tagName);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getGiftCertificateByTagName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByName(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByName(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending)
            throws ServiceException {
        try {
            return giftCertificateDAO.getAllGiftCertificatesSortedByDate(isAscending);
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in getAllGiftCertificatesSortedByDate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_RETRIEVE_CERTIFICATE.getCode()));
        }
    }
    @Override
    public List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws ServiceException {
        if (requestBody.getContent() != null) {
            return getAllGiftCertificates(requestBody.getContent());
        }
        if (requestBody.getSort() != null) {
            return getSortedCertificates(requestBody.getSort());
        }
        if (requestBody.getTagName() != null) {
            return getGiftCertificateByTagName(requestBody.getTagName());
        }
        throw new ServiceException("Wrong request input", new ErrorCode(ErrorCodeEnum.INVALID_SORT_INPUT.getCode()));
    }

    private List<GiftCertificate> getSortedCertificates(String sort) throws ServiceException {
        int separator = sort.indexOf(".");
        if (separator != -1 && separator < sort.length()) {
            String sortBy = sort.substring(0 , separator).toUpperCase();
            String sortType = sort.substring(++separator).toUpperCase();

            if (!EnumUtils.isValidEnum(SortParameter.class, sortBy)) {
                throw new ServiceException("Wrong sort input",
                        new ErrorCode(ErrorCodeEnum.INVALID_SORT_INPUT.getCode()));
            }

            return switch (SortParameter.valueOf(sortBy)) {
                case DATE -> getAllGiftCertificatesSortedByDate(isAscending(sortType));
                case NAME -> getAllGiftCertificatesSortedByName(isAscending(sortType));
            };
        } else {
            throw new ServiceException("Wrong sort input", new ErrorCode(ErrorCodeEnum.INVALID_SORT_INPUT.getCode()));
        }
    }

    private boolean isAscending(String sort) throws ServiceException {
        if (!EnumUtils.isValidEnum(SortType.class, sort)) {
            throw new ServiceException("Wrong sort input",
                    new ErrorCode(ErrorCodeEnum.INVALID_SORT_INPUT.getCode()));
        }

        return switch (SortType.valueOf(sort)) {
            case ASC -> true;
            case DESC -> false;
        };
    }

    @Override
    @Transactional
    public int addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        CertificateValidator.validateCertificate(giftCertificate);
        try {
            giftCertificate.setId(giftCertificateDAO.addGiftCertificate(giftCertificate));

            addNewTagsToCertificate(giftCertificate);

            return giftCertificate.getId();
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in addGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE.getCode()));
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown: " + e.getMessage());
            throw new ServiceException("Failed to return certificate id",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_ADD_CERTIFICATE.getCode()));
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
                throw new ServiceException("Failed to delete certificate",
                        new ErrorCode(ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE.getCode() + giftCertificate.getId()));
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in deleteGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE.getCode() + giftCertificate.getId()));
        }
    }

    @Override
    @Transactional
    public void updateGiftCertificate(GiftCertificate giftCertificate) throws ServiceException {
        CertificateValidator.validateCertificate(giftCertificate);
        try {
            addNewTagsToCertificate(giftCertificate);

            if (!giftCertificateDAO.updateGiftCertificate(giftCertificate)) {
                LOGGER.error("Failed to update certificate");
                throw new ServiceException("Failed to update certificate",
                        new ErrorCode(ErrorCodeEnum.FAILED_TO_DELETE_CERTIFICATE.getCode() + giftCertificate.getId()));
            }
        } catch (DataAccessException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to get certificate",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE.getCode() + giftCertificate.getId()));
        } catch (PersistenceException e) {
            LOGGER.error("Following exception was thrown in updateGiftCertificate(): " + e.getMessage());
            throw new ServiceException("Failed to return certificate id",
                    new ErrorCode(ErrorCodeEnum.FAILED_TO_UPDATE_CERTIFICATE.getCode() + giftCertificate.getId()));
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
