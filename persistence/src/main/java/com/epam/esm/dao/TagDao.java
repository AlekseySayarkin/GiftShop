package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.model.Tag;
import java.util.List;

/**
 * This interface provides with ability to
 * transfer {@code Tag} in and out
 * of data source.
 *
 * @author Aleksey Sayarkin
 */
public interface TagDao {

    /**
     * Retrieves data of {@code Tag} from
     * data source by name
     * which equals to {@code String name}.
     *
     * @param name tag name.
     * @return {@code Tag}.
     */
    Tag getTag(String name);

    /**
     * Retrieves data of {@code Tag} from
     * data source by id
     * which equals to {@code int id}.
     *
     * @param id tag id.
     * @return {@code Tag}.
     */
    Tag getTag(int id);

    /**
     * Retrieves all {@code Tag} from data source.
     *
     * @return List<Tag> - all existing tags in data source.
     */
    List<Tag> getAllTags();

    /**
     * Adds new {@code Tag} to data source.
     *
     * @param tag {@code Tag} which to be added to data source.
     * @return id of a {@code Tag} from data source.
     * @throws PersistenceException when failed to add {@code Tag} to data source.
     */
    int addTag(Tag tag) throws PersistenceException;

    /**
     * Deletes {@code Tag} from data source.
     *
     * @param tagId id of a {@code Tag} which to delete from data source.
     * @return whether transaction was successful.
     */
    boolean deleteTag(int tagId);
}
