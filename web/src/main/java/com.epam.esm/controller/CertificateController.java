package com.epam.esm.controller;

import com.epam.esm.dao.exception.ErrorCode;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exception.ServiceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CertificateController {

    private static final int SORT_INPUT_ERROR_CODE = 409;
    private static final int FAILED_TO_DELETE = 503;
    private static final int FAILED_TO_UPDATE = 504;

    private final GiftCertificateService giftCertificateService;

    @Autowired
    public CertificateController(GiftCertificateService giftCertificateService) {
        this.giftCertificateService = giftCertificateService;
    }

    @GetMapping("/certificates")
    public List<GiftCertificate> getGiftCertificates(@RequestBody(required = false) String request)
            throws ServiceException {
        if (request != null) {
           return getCertificates(request);
        } else {
            return giftCertificateService.getAllGiftCertificates();
        }
    }

    private List<GiftCertificate> getCertificates(String request) throws ServiceException {
        JSONObject jsonObject = new JSONObject(request);
        if (jsonObject.has("content")) {
            return giftCertificateService.getAllGiftCertificates(jsonObject.getString("content"));
        }
        if (jsonObject.has("sort")) {
            return getSortedCertificates(jsonObject.getString("sort"));
        }
        if (jsonObject.has("tagName")) {
            return giftCertificateService.getGiftCertificateByTagName(jsonObject.getString("tagName"));
        }
        throw new ServiceException("Wrong request input", new ErrorCode(SORT_INPUT_ERROR_CODE));
    }

    private List<GiftCertificate> getSortedCertificates(String sort) throws ServiceException {
        int separator = sort.indexOf(".");
        if (separator != -1 && separator < sort.length()) {
            String sortBy = sort.substring(0 , separator);
            String sortType = sort.substring(++separator);

            return switch (sortBy) {
                case "date" -> giftCertificateService.getAllGiftCertificatesSortedByDate(isAscending(sortType));
                case "name" -> giftCertificateService.getAllGiftCertificatesSortedByName(isAscending(sortType));
                default -> throw new ServiceException("Wrong sort input", new ErrorCode(SORT_INPUT_ERROR_CODE));
            };
        } else {
            throw new ServiceException("Wrong sort input", new ErrorCode(SORT_INPUT_ERROR_CODE));
        }
    }

    private boolean isAscending(String sort) throws ServiceException {
        return switch (sort) {
            case "asc" -> true;
            case "desc" -> false;
            default -> throw new ServiceException("Wrong sort input", new ErrorCode(SORT_INPUT_ERROR_CODE));
        };
    }

    @GetMapping("/certificates/name")
    public GiftCertificate getCertificateByName(@RequestBody String name) throws ServiceException {
        return giftCertificateService.getGiftCertificate(new JSONObject(name).getString("name"));
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
