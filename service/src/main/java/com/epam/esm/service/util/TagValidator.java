package com.epam.esm.service.util;

import com.epam.esm.model.Tag;

public class TagValidator {

    private TagValidator() {
    }

    public static boolean isValid(Tag tag) {
        if (tag == null || tag.getId() < 0) {
            return false;
        }

        return tag.getName() != null && !tag.getName().isEmpty();
    }
}
