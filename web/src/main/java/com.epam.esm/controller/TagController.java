package com.epam.esm.controller;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.exception.WebException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TagController {

    private static final int FAILED_TO_DELETE = 503;

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    public List<Tag> getTags() {
        try {
            return tagService.getAllTags();
        } catch (ServiceException e) {
            throw new WebException("Tags not found", e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags/{id}")
    public Tag getTag(@PathVariable int id) {
        try {
            return tagService.getTag(id);
        } catch (ServiceException e) {
            throw new WebException("Tag not found", e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags/name")
    public Tag findByName(@RequestBody String name) {
        try {
            return tagService.getTag(new JSONObject(name).getString("name"));
        } catch (ServiceException e) {
            throw new WebException("Tag not found", e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tags")
    public Tag addTag(@RequestBody Tag tag) {
        try {
            tag.setId(tagService.addTag(tag));
            return tag;
        } catch (ServiceException e) {
            throw new WebException("Failed to add tag", e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tags/{id}")
    public HttpStatus deleteTag(@PathVariable int id) {
        try {
            if (tagService.deleteTag(id)) {
                return HttpStatus.OK;
            } else {
                throw new WebException("Failed to delete tag",
                        new ErrorCode(Integer.parseInt(FAILED_TO_DELETE + String.valueOf(id))),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (ServiceException e) {
            throw new WebException("Failed to delete tag", e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
