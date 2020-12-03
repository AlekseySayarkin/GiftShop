package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.GiftCertificate;

import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code GiftCertificate} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface GiftCertificateDAO {

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param name certificate name.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(String name);

    /**
     * Retrieves data of {@code GiftCertificate} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id certificate id.
     * @return {@code GiftCertificate}.
     */
    GiftCertificate getGiftCertificate(int id);

    /**
     * Retrieves all {@code GiftCertificate} from data source.
     *
     * @return List<GiftCertificate> - all existing certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificates();

    /**
     * Retrieves {@code GiftCertificate} from data source
     * by content which this {@code GiftCertificate} contains
     * in it name or description.
     *
     * @param content {@code GiftCertificate} name or description.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificates(String content);

    /**
     * Retrieves {@code GiftCertificate} from data source
     * by name of a {@code Tag} which this {@code GiftCertificate} has.
     *
     * @param tagName name of a {@code Tag}.
     * @return List<GiftCertificate> - existing certificates in data source.
     */
    List<GiftCertificate> getGiftCertificateByTagName(String tagName);

    /**
     * Retrieves all {@code GiftCertificate} from data source
     * and sorts it by name according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByName(boolean isAscending);

    /**
     * Retrieves all {@code GiftCertificate} from data source
     * and sorts it by date according to {@code isAscending}.
     *
     * @param isAscending asc or desc sort.
     * @return List<GiftCertificate> - sorted certificates in data source.
     */
    List<GiftCertificate> getAllGiftCertificatesSortedByDate(boolean isAscending);

    /**
     * Adds new {@code GiftCertificate} to data source.
     *
     * @param giftCertificate {@code GiftCertificate} which to be added to data source.
     * @return id of a {@code GiftCertificate} from data source.
     * @throws PersistenceException when failed to add {@code GiftCertificate} to data source.
     */
    int addGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Deletes {@code GiftCertificate} from data source.
     *
     * @param id id of {@code GiftCertificate} which to deleted from data source.
     * @return whether transaction was successful.
     */
    boolean deleteGiftCertificate(int id);

    /**
     * Updates {@code GiftCertificate} in data source.
     * Null or default values in {@code GiftCertificate} are not updated.
     *
     * @param giftCertificate {@code GiftCertificate} which to update in data source.
     * @return whether transaction was successful.
     */
    boolean updateGiftCertificate(GiftCertificate giftCertificate) throws PersistenceException;

    /**
     * Creates many to many relation with {@code GiftCertificate} and {@code Tag}.
     *
     * @param certificateId {@code GiftCertificate} id which to create a many to many relation with.
     * @param tagId {@code Tag} id  which to create a many to many relation with.
     * @return whether transaction was successful.
     */
    boolean createCertificateTagRelation(int certificateId, int tagId);

    /**
     * Deletes many to many relation with {@code GiftCertificate} and {@code Tag}.
     *
     * @param certificateId {@code GiftCertificate} id which to delete a many to many relation with.
     * @return whether transaction was successful.
     */
    boolean deleteAllCertificateTagRelations(int certificateId);
}
