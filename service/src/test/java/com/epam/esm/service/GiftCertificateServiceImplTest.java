package com.epam.esm.service;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.impl.SQLGiftCertificateDaoImpl;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.request.CertificateRequestBody;
import com.epam.esm.service.request.SortParameter;
import com.epam.esm.service.request.SortType;
import com.epam.esm.service.util.impl.CertificateValidatorImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiftCertificateServiceImplTest {

    private TagDao tagDao;
    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;

    @Before
    public void init() {
        tagDao = Mockito.mock(SQLTagDaoImpl.class);
        giftCertificateDAO = Mockito.mock(SQLGiftCertificateDaoImpl.class);

        giftCertificateService = new GiftCertificateServiceImpl(
                giftCertificateDAO, tagDao, new CertificateValidatorImpl());
    }

    private List<GiftCertificate> initCertificates() {
        List<GiftCertificate> certificates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            certificates.add(certificate);
        }

        return certificates;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag(1, "spa");
        giftCertificate.getTags().add(tag);

        return giftCertificate;
    }

    @Test
    public void whenGetCertificate_thenCorrectlyReturnsItById() throws ServiceException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.getGiftCertificate(given.getId())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificate(given.getId());
        Assert.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(given.getId());
    }

    @Test
    public void whenGetCertificate_thenCorrectlyReturnsItByName() throws ServiceException {
        GiftCertificate given = new GiftCertificate();
        given.setId(1);
        given.setName("Tourism");

        Mockito.when(giftCertificateDAO.getGiftCertificate(given.getName())).thenReturn(given);

        GiftCertificate actual = giftCertificateService.getGiftCertificate(given.getName());
        Assert.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(given.getName());
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThem() throws ServiceException {
        List<GiftCertificate> given = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(i);
            certificate.setName("Tag " + i);
            given.add(certificate);
        }

        Mockito.when(giftCertificateService.geAllCertificatesByContent()).thenReturn(given);

        List<GiftCertificate> actual = giftCertificateService.geAllCertificatesByContent();
        Assert.assertEquals(given, actual);
        Mockito.verify(giftCertificateDAO).getAllGiftCertificates();
    }

    @Test
    public void whenAddCertificate_thenReturnItId() throws PersistenceException, ServiceException {
        GiftCertificate givenCertificate = initCertificate();
        Tag givenTag = new Tag(1,"spa");
        int expectedId = 1;

        Mockito.when(tagDao.addTag(givenTag)).thenReturn(1);
        Mockito.when(giftCertificateDAO.createCertificateTagRelation(
                givenCertificate.getId(), givenTag.getId())).thenReturn(true);
        Mockito.when(giftCertificateDAO.addGiftCertificate(givenCertificate)).thenReturn(expectedId);

        givenCertificate = giftCertificateService.addGiftCertificate(givenCertificate);
        Assert.assertEquals(expectedId, givenCertificate.getId());
        Mockito.verify(tagDao).addTag(givenTag);
        Mockito.verify(giftCertificateDAO)
                .createCertificateTagRelation(givenCertificate.getId(), givenCertificate.getId());
        Mockito.verify(giftCertificateDAO).addGiftCertificate(givenCertificate);
    }

    @Test
    public void whenTryAddVoidCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificateService.addGiftCertificate(giftCertificate);
        } catch (ServiceException e) {
            Assert.assertEquals("Failed to validate: certificate name is empty", e.getMessage());
        }
    }

    @Test
    public void whenTryDeleteCertificate_thenThrowException() {
        GiftCertificate givenCertificate = initCertificate();
        Tag givenTag = new Tag(1,"spa");

        try {
            giftCertificateService.deleteGiftCertificate(givenCertificate);
        } catch (ServiceException e) {
            Assert.assertEquals("Failed to delete certificate because it id ("
                    + givenCertificate.getId() + ") is not found", e.getMessage());
        }

        Mockito.verify(giftCertificateDAO).deleteCertificateTagRelation(givenCertificate.getId(), givenTag.getId());
        Mockito.verify(giftCertificateDAO).deleteGiftCertificate(givenCertificate.getId());
    }

    @Test
    public void whenAddCertificate_thenReturnThemSortedByDateAsc() throws ServiceException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.ASC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        List<GiftCertificate> givenCertificates = initCertificates();

        Mockito.when(giftCertificateDAO.getAllGiftCertificatesSortedByDate(true))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificates(givenRequestBody);
        Assert.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getAllGiftCertificatesSortedByDate(true);
    }

    @Test
    public void whenAddCertificate_thenReturnThemSortedByDateDesc() throws ServiceException {
        CertificateRequestBody givenRequestBody = new CertificateRequestBody();
        givenRequestBody.setSortType(SortType.DESC);
        givenRequestBody.setSortBy(SortParameter.DATE);
        List<GiftCertificate> givenCertificates = initCertificates();
        Collections.reverse(givenCertificates);

        Mockito.when(giftCertificateDAO.getAllGiftCertificatesSortedByDate(false))
                .thenReturn(givenCertificates);

        List<GiftCertificate> actual = giftCertificateService.getGiftCertificates(givenRequestBody);
        Assert.assertEquals(givenCertificates, actual);
        Mockito.verify(giftCertificateDAO).getAllGiftCertificatesSortedByDate(false);
    }
}
