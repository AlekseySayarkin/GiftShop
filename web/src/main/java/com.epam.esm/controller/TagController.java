package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public String home(Model model) throws ServiceException {
        model.addAttribute("tags", tagService.getAllTags());
        return "home";
    }

    @PostMapping("/tags/add")
    public String add(@ModelAttribute("Tag") Tag tag) throws ServiceException {
        if (tag.getId() == 0) {
            tagService.addTag(tag);
        }
        return "redirect:/home";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) throws ServiceException {
        tagService.deleteTag(id);
        return "redirect:/home";
    }

    @GetMapping("/tag/{id}")
    public String get(@PathVariable int id, Model model) throws ServiceException {
        model.addAttribute("tag", tagService.getTag(id));
        return "home";
    }
}
