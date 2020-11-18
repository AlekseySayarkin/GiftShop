package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

/**
 * This interface provides with ability to create
 * transactions with {@code Tag} in and out
 * of persistence layer.
 *
 * @author Aleksey Sayarkin
 */
public interface TagService {

    /**
     * Retrieves data of {@code Tag} from
     * persistence layer by name
     * which equals to {@code String name}.
     *
     * @param name tag name.
     * @throws ServiceException when failed to get {@code Tag} from persistence layer.
     * @return {@code Tag}.
     */
    Tag getTag(String name) throws ServiceException;

    /**
     * Retrieves data of {@code Tag} from
     * persistence layer by id
     * which equals to {@code int id}.
     *
     * @param id tag id.
     * @throws ServiceException when failed to get {@code Tag} from persistence layer.
     * @return {@code Tag}.
     */
    Tag getTag(int id) throws ServiceException;

    /**
     * Retrieves all {@code Tag} from persistence layer.
     *
     * @throws ServiceException when failed to get {@code Tag} from persistence layer.
     * @return List<Tag> - all existing tags in persistence layer.
     */
    List<Tag> getAllTags() throws ServiceException;

    /**
     * Adds new {@code Tag} to persistence layer.
     *
     * @param tag {@code Tag} which to be added to persistence layer.
     * @throws ServiceException when failed to add {@code Tag} to persistence layer.
     * @return id of a {@code Tag} from persistence layer.
     */
    Tag addTag(Tag tag) throws ServiceException;

    /**
     * Deletes {@code Tag} from persistence layer.
     *
     * @param tagId id of a {@code Tag} which to delete from persistence layer.
     * @throws ServiceException when failed to delete {@code Tag} from persistence layer.
     */
    void deleteTag(int tagId) throws ServiceException;
}
