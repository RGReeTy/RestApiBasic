package com.epam.esm.service;

import com.epam.esm.model.dto.TagDto;

import java.util.List;

public interface TagService {

    TagDto getTag(Long id);

    List<TagDto> getAllTags();

    Long createTag(TagDto tag);

    void deleteTag(Long id);
}
