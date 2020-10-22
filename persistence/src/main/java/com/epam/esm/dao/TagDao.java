package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDao {

    Tag getTagByName(String name);
    Tag getTagById(int id);
    List<Tag> getTags();

    int addTag(Tag tag);
    boolean deleteTag(Tag tag);
}
