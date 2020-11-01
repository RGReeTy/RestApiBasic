package com.epam.esm.repo;

import com.epam.esm.model.Tag;

import java.util.List;

public interface TagRepo {

    long create(Tag tag);

    Tag findById(Long id);

    Tag findByName(String name);

    List<Tag> getAll();

    void delete(Long id);
}
