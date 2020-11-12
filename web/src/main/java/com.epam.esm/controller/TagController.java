package com.epam.esm.controller;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.exception.WebException;
import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class TagController {

    private static final int FAILED_TO_DELETE = 503;

    private final MessageSource messageSource;
    private final CookieLocaleResolver cookieLocaleResolver;
    private final TagService tagService;

    @Autowired
    public TagController(
            TagService tagService, CookieLocaleResolver cookieLocaleResolver, MessageSource messageSource) {
        this.tagService = tagService;
        this.cookieLocaleResolver = cookieLocaleResolver;
        this.messageSource = messageSource;
    }

    @GetMapping(value = "/tags")
    public List<Tag> getTags(HttpServletRequest request) {
        try {
            return tagService.getAllTags();
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.tagsNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/tags/tag/{id}")
    public Tag getTag(@PathVariable int id, HttpServletRequest request) {
        try {
            return tagService.getTag(id);
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.tagNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/tags")
    public Tag addTag(@RequestBody Tag tag, HttpServletRequest request) {
        try {
            tag.setId(tagService.addTag(tag));
            return tag;
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.failedToAddTag", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/tags/tag/{id}")
    public HttpStatus deleteTag(@PathVariable int id, HttpServletRequest request) {
        try {
            if (tagService.deleteTag(id)) {
                return HttpStatus.OK;
            } else {
                throw new ServiceException(new ErrorCode(Integer.parseInt(FAILED_TO_DELETE + String.valueOf(id))));
            }
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.failedToDeleteTag", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
