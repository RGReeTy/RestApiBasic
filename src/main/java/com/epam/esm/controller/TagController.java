package com.epam.esm.controller;

import com.epam.esm.model.Tag;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping("/{id}")
    public Tag getTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    @GetMapping
    @ResponseBody
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTag(@RequestBody Tag tag) {
        tagService.createTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
    }
}
