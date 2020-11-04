package com.epam.esm.controller;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.FilterDto;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class GiftController {

    private final GiftCertificateService service;


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificateDto getGiftCertificateById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    @ResponseBody
    public List<GiftCertificateDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createGiftCertificate(@RequestBody GiftCertificateDto certificate) {
        service.createGiftCertificate(certificate);
    }

    @DeleteMapping("/{id}")
    public void deleteGiftCertificate(@PathVariable("id") Long id) {
        service.deleteGiftCertificate(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateGiftCertificate(@PathVariable("id") Long id,
                                      @RequestBody GiftCertificateDto certificate) {
        certificate.setId(id);
        service.updateGiftCertificate(certificate);
    }

    @GetMapping("/filter")
    public List<GiftCertificateDto> GiftfilterCertificates(FilterDto filter) {
        return service.filterGiftCertificate(filter);
    }

    @PutMapping("/{certificateId}/tags/{tagId}")
    public void GiftLinkTagToCertificate(@PathVariable("certificateId") Long certificateId, @PathVariable("tagId") Long tagId) {
        service.linkTagToGiftCertificate(certificateId, tagId);
    }
}
