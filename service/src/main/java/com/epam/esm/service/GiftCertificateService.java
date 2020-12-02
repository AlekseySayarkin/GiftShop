package com.epam.esm.service;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.request.CertificateRequestBody;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code GiftCertificate} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface GiftCertificateService {

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param name certificate name.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(String name) throws ServiceException;

    /**
     * Retrieves data of {@code GiftCertificate} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(int id) throws ServiceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - all existing certificates in persistence layer.
     */
    List<GiftCertificate> geAllCertificatesByContent() throws ServiceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by content which this {@code GiftCertificate} contains
     * in it name or description.
     *
     * @param content {@code GiftCertificate} name or description.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificatesByContent(String content) throws ServiceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * by name of a {@code Tag} which this {@code GiftCertificate} has.
     *
     * @param tagName name of a {@code Tag}.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - existing certificates in persistence layer.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName) throws ServiceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by name according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     *@throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending) throws ServiceException;

    /**
     * Retrieves all {@code GiftCertificate} from persistence layer
     * and sorts it by date according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - sorted certificates in persistence layer.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending) throws ServiceException;

    /**
     * Retrieves {@code GiftCertificate} from persistence layer
     * using one of non null fields of {@code CertificateRequestBody}.
     *
     * @param requestBody representation of http request body.
     * @throws ServiceException when failed to get {@code GiftCertificate} from persistence layer.
     * @return List<GiftCertificate> - certificates from persistence layer.
     */
    List<GiftCertificate> getGiftCertificates(CertificateRequestBody requestBody) throws ServiceException;

    /**
     * Adds new {@code GiftCertificate} to persistence layer.
     *
     * @param giftCertificate {@code GiftCertificate} which to add to persistence layer.
     * @throws ServiceException when failed to add {@code GiftCertificate} to persistence layer.
     * @return {@code GiftCertificate} from persistence layer.
     */
    GiftCertificate addGiftCertificate(GiftCertificate giftCertificate) throws ServiceException;

    /**
     * Deletes {@code GiftCertificate} from persistence layer.
     *
     * @param id id of {@code GiftCertificate} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code GiftCertificate} from persistence layer.
     */
    void deleteGiftCertificate(int id) throws ServiceException;

    /**
     * Updates {@code GiftCertificate} in persistence layer.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code GiftCertificate} which to update in persistence layer.
     * @param id id of {@code GiftCertificate} which to update in persistence layer.
     * @throws ServiceException when failed to update {@code GiftCertificate} in persistence layer.
     * @return updated {@code GiftCertificate}
     */
    GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate, int id) throws ServiceException;
}
