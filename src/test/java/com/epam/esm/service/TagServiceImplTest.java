package com.epam.esm.service;

import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.mapper.TagMapper;
import com.epam.esm.repo.TagRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {

    @Mock
    private TagRepo mockTagRepo;
    @Mock
    private TagMapper tagMapper;
    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void getTag() {
        Tag tag = Tag
                .builder()
                .id(1L)
                .name("name")
                .build();

        TagDto tagDto = TagDto
                .builder()
                .id(1L)
                .name("name")
                .build();

        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagRepo.findById(1L)).thenReturn(tag);
        Assertions.assertEquals(tagDto, tagService.getTag(1L));
    }

    @Test
    void getAllTags() {
        Tag tag = Tag.builder().id(1L).name("name").build();
        TagDto tagDto = TagDto.builder().id(1L).name("name").build();
        List<TagDto> tagDtoList = Collections.singletonList(tagDto);
        List<Tag> tagList = Collections.singletonList(tag);
        Mockito.when(tagMapper.fromModelWithoutCertificate(tag)).thenReturn(tagDto);
        Mockito.when(mockTagRepo.getAll()).thenReturn(tagList);
        Assertions.assertEquals(tagDtoList, tagService.getAllTags());
    }

    @Test
    void createTag() {
        Tag tag = Tag.builder().name("names").build();
        TagDto tagDto = TagDto.builder().id(5L).name("names").build();
        Mockito.when(tagMapper.toModelWithoutCertificate(tagDto)).thenReturn(tag);
        Mockito.when(mockTagRepo.findByName(tag.getName())).thenThrow(EntityNotFoundException.class);
        Mockito.when(mockTagRepo.create(tag)).thenReturn(5L);
        Assertions.assertEquals(5, tagService.createTag(tagDto));
    }

    @Test
    void deleteTag() {
        Mockito.doNothing().when(mockTagRepo).delete(5L);
        tagService.deleteTag(5L);
        Mockito.verify(mockTagRepo, times(1)).delete(5L);
    }
}