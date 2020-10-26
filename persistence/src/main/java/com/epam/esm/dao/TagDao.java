package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import java.util.List;

public interface TagDao {

    Tag getTag(String name);
    Tag getTag(int id);
    List<Tag> getAllTags();

    int addTag(Tag tag);
    boolean deleteTag(int tagId);
}
