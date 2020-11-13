package com.epam.esm.controller;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.exception.WebException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class CertificateController {

    private static final int SORT_INPUT_ERROR_CODE = 409;
    private static final int FAILED_TO_DELETE = 503;
    private static final int FAILED_TO_UPDATE = 504;

    private final MessageSource messageSource;
    private final CookieLocaleResolver cookieLocaleResolver;
    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(
            MessageSource messageSource, CookieLocaleResolver cookieLocaleResolver,
            GiftCertificateService giftCertificateService) {
        this.messageSource = messageSource;
        this.cookieLocaleResolver = cookieLocaleResolver;
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public List<GiftCertificate> getGiftCertificates(HttpServletRequest request) {
        try {
            return giftCertificateService.getAllGiftCertificates();
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificatesNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/certificates/certificate/{id}")
    public GiftCertificate getGiftCertificate(
            @PathVariable int id, HttpServletRequest request) {
        try {
            return giftCertificateService.getGiftCertificate(id);
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificateNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/certificates/certificate/content")
    public List<GiftCertificate> getGiftCertificatesByContent(
            @RequestBody String content, HttpServletRequest request) {
        try {
            return giftCertificateService.getAllGiftCertificates(new JSONObject(content).getString("content"));
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificatesNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/certificates/certificate/tag")
    public List<GiftCertificate> getGiftCertificatesByTagNae(
            @RequestBody String name, HttpServletRequest request) {
        try {
            return giftCertificateService.getGiftCertificateByTagName(new JSONObject(name).getString("name"));
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificatesNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/certificates/certificate/sort/name/{sort}")
    public List<GiftCertificate> getGiftCertificatesSortedByName(
            @PathVariable String sort, HttpServletRequest request) {
        try {
            return giftCertificateService.getAllGiftCertificatesSortedByName(isAscending(sort));
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificatesNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/certificates/certificate/sort/date/{sort}")
    public List<GiftCertificate> getGiftCertificatesSortedByDate(
            @PathVariable String sort, HttpServletRequest request) {
        try {
            return giftCertificateService.getAllGiftCertificatesSortedByDate(isAscending(sort));
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.certificatesNotFound", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.NOT_FOUND);
        }
    }

    private boolean isAscending(String sort) throws ServiceException{
        return switch (sort) {
            case "asc" -> true;
            case "desc" -> false;
            default -> throw new ServiceException(new ErrorCode(SORT_INPUT_ERROR_CODE));
        };
    }

    @PostMapping("/certificates")
    public GiftCertificate addGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, HttpServletRequest request) {
        try {
            giftCertificate.setId(giftCertificateService.addGiftCertificate(giftCertificate));
            return giftCertificate;
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.failedToAddCertificate", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/certificates/certificate")
    public HttpStatus deleteGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, HttpServletRequest request) {
        try {
            if (giftCertificateService.deleteGiftCertificate(giftCertificate)) {
                return HttpStatus.OK;
            } else {
                throw new ServiceException(new ErrorCode(
                        Integer.parseInt(FAILED_TO_DELETE + String.valueOf(giftCertificate.getId()))));
            }
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.failedToDeleteCertificate", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/certificates/certificate")
    public HttpStatus updateGiftCertificate(
            @RequestBody GiftCertificate giftCertificate, HttpServletRequest request) {
        try {
            if (giftCertificateService.updateGiftCertificate(giftCertificate)) {
                return HttpStatus.OK;
            } else {
                throw new ServiceException(new ErrorCode(
                        Integer.parseInt(FAILED_TO_UPDATE + String.valueOf(giftCertificate.getId()))));
            }
        } catch (ServiceException e) {
            String message = messageSource.getMessage(
                    "error.failedToUpdateCertificate", null, cookieLocaleResolver.resolveLocale(request));
            throw new WebException(message, e.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
