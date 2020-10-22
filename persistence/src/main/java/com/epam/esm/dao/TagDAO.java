package com.epam.esm.dao;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagDAO {

    Tag getTagByName(String name);
    Tag getTagById(int id);
    List<Tag> getTags();

    boolean addTag(Tag tag);
    boolean deleteTag(Tag tag);
}
