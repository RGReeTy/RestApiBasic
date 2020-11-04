package com.epam.esm.controller;

import com.epam.esm.model.dto.TagDto;
import com.epam.esm.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TagDto getTag(@PathVariable("id") Long id) {
        return tagService.getTag(id);
    }

    @GetMapping
    @ResponseBody
    public List<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTag(@RequestBody TagDto tag) {
        tagService.createTag(tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable("id") Long id) {
        tagService.deleteTag(id);
    }
}
