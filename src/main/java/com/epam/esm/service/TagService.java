package com.epam.esm.service;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagService {

    Tag getTag(Long id);

    List<Tag> getAllTags();

    Long createTag(Tag tag);

    void deleteTag(Long id);
}
