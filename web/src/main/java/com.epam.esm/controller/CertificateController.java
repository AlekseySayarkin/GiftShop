package com.epam.esm.controller;

import com.epam.esm.service.CertificateRequestBody;
import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CertificateController {

    private static final int FAILED_TO_DELETE = 503;
    private static final int FAILED_TO_UPDATE = 504;

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public List<GiftCertificate> getGiftCertificates(
            @RequestBody(required = false) CertificateRequestBody request) throws ServiceException {
        if (request != null) {
           return giftCertificateService.getGiftCertificates(request);
        } else {
            return giftCertificateService.getAllGiftCertificates();
        }
    }

    @GetMapping("/certificates/{id}")
    public GiftCertificate getGiftCertificate(@PathVariable int id) throws ServiceException {
        return giftCertificateService.getGiftCertificate(id);
    }

    @PostMapping("/certificates")
    public GiftCertificate addGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        giftCertificate.setId(giftCertificateService.addGiftCertificate(giftCertificate));
        return giftCertificate;
    }

    @DeleteMapping("/certificates")
    public HttpStatus deleteGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificateService.deleteGiftCertificate(giftCertificate)) {
            return HttpStatus.OK;
        } else {
            throw new ServiceException(new ErrorCode(
                    Integer.parseInt(FAILED_TO_DELETE + String.valueOf(giftCertificate.getId()))));
        }
    }

    @PutMapping("/certificates")
    public HttpStatus updateGiftCertificate(@RequestBody GiftCertificate giftCertificate) throws ServiceException {
        if (giftCertificateService.updateGiftCertificate(giftCertificate)) {
            return HttpStatus.OK;
        } else {
            throw new ServiceException("Failed to update certificate",
                    new ErrorCode(Integer.parseInt(FAILED_TO_UPDATE + String.valueOf(giftCertificate.getId()))));
        }
    }
}
