package com.epam.esm.controller;

import com.epam.esm.model.Filter;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class GiftController {

    private final GiftCertificateService service;


    @GetMapping("/{id}")
    public GiftCertificate getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    @ResponseBody
    public List<GiftCertificate> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createGiftCertificate(@RequestBody GiftCertificate certificate) {
        service.createGiftCertificate(certificate);
    }

    @DeleteMapping("/{id}")
    public void deleteGiftCertificate(@PathVariable("id") Long id) {
        service.deleteGiftCertificate(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateGiftCertificate(@PathVariable("id") Long id,
                                      @RequestBody GiftCertificate certificate) {
        certificate.setId(id);
        service.updateGiftCertificate(certificate);
    }

    @GetMapping("/filter")
    public List<GiftCertificate> GiftfilterCertificates(Filter filter) {
        return service.filterGiftCertificate(filter);
    }

    @PutMapping("/{certificateId}/tags/{tagId}")
    public void GiftlinkTagToCertificate(@PathVariable("certificateId") Long certificateId, @PathVariable("tagId") Long tagId) {
        service.linkTagToGiftCertificate(certificateId, tagId);
    }
}
