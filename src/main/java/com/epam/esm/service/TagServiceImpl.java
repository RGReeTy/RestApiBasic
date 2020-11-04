package com.epam.esm.service;

import com.epam.esm.exception.EntityAlreadyExistException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.dto.mapper.TagMapper;
import com.epam.esm.repo.TagRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepo tagRepo;
    private final TagMapper tagMapper;

    @Override
    public TagDto getTag(Long id) {
        return tagMapper.fromModelWithoutCertificate(tagRepo.findById(id));
    }

    @Override
    public List<TagDto> getAllTags() {
        return tagRepo
                .getAll()
                .stream()
                .map(tagMapper::fromModelWithoutCertificate)
                .collect(Collectors.toList());
    }

    @Override
    public Long createTag(TagDto tag) {
        try {
            Tag byName = tagRepo.findByName(tag.getName());
            throw new EntityAlreadyExistException("Resource already exists", byName.getId());
        } catch (EntityNotFoundException ex) {
            return tagRepo.create(tagMapper.toModelWithoutCertificate(tag));
        }
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepo.delete(id);
    }
}
