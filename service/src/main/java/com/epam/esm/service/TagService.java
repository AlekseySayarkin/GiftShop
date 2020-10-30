package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;

import java.util.List;

public interface TagService {
    Tag getTag(String name) throws ServiceException;

    Tag getTag(int id) throws ServiceException;

    List<Tag> getAllTags() throws ServiceException;

    int addTag(Tag tag) throws ServiceException;

    boolean deleteTag(int tagId) throws ServiceException;
}
