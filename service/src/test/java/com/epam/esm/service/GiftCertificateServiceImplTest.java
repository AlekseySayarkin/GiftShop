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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class GiftCertificateServiceImplTest {

    private TagDao tagDao;
    private GiftCertificateDAO giftCertificateDAO;
    private GiftCertificateService giftCertificateService;

    @Before
    public void init() {
        tagDao = Mockito.mock(SQLTagDaoImpl.class);
        giftCertificateDAO = Mockito.mock(SQLGiftCertificateDaoImpl.class);

        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDAO, tagDao);
    }

    @Test
    public void whenGetCertificate_thenCorrectlyReturnsIt() throws ServiceException {
        GiftCertificate expected;
        GiftCertificate actual;

        expected = new GiftCertificate();
        expected.setId(1);
        expected.setName("Tourism");

        Mockito.when(giftCertificateDAO.getGiftCertificate(expected.getId())).thenReturn(expected);
        Mockito.when(giftCertificateDAO.getGiftCertificate(expected.getName())).thenReturn(expected);

        actual = giftCertificateService.getGiftCertificate(expected.getId());
        Assert.assertEquals(expected, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(expected.getId());

        actual = giftCertificateService.getGiftCertificate(expected.getName());
        Assert.assertEquals(expected, actual);
        Mockito.verify(giftCertificateDAO).getGiftCertificate(expected.getName());
    }

    @Test
    public void whenReturnAllCertificates_thenCorrectlyReturnAllCertificates() throws ServiceException {
        List<GiftCertificate> expected;
        List<GiftCertificate> actual;

        expected = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setId(i);
            certificate.setName("Tag " + i);
            expected.add(certificate);
        }

        Mockito.when(giftCertificateService.getAllGiftCertificates()).thenReturn(expected);
        actual = giftCertificateService.getAllGiftCertificates();

        Assert.assertEquals(expected, actual);
        Mockito.verify(giftCertificateDAO).getAllGiftCertificates();
    }

    @Test
    public void whenAddCertificate_thenReturnItId() throws PersistenceException, ServiceException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag("spa");
        giftCertificate.getTags().add(tag);
        int expectedId = 1;

        Mockito.when(tagDao.addTag(tag)).thenReturn(1);
        Mockito.when(giftCertificateDAO.createCertificateTagRelation(
                giftCertificate.getId(), tag.getId())).thenReturn(true);
        Mockito.when(giftCertificateDAO.addGiftCertificate(giftCertificate)).thenReturn(expectedId);
        giftCertificate.setId(giftCertificateService.addGiftCertificate(giftCertificate));

        Assert.assertEquals(expectedId, giftCertificate.getId());
        Mockito.verify(tagDao).addTag(tag);
        Mockito.verify(giftCertificateDAO).createCertificateTagRelation(giftCertificate.getId(), tag.getId());
        Mockito.verify(giftCertificateDAO).addGiftCertificate(giftCertificate);
    }

    @Test
    public void whenTryAddVoidCertificate_thenThrowException() {
        GiftCertificate giftCertificate = new GiftCertificate();

        try {
            giftCertificate.setId(giftCertificateService.addGiftCertificate(giftCertificate));
        } catch (ServiceException e) {
            Assert.assertEquals("Invalid certificate", e.getMessage());
        }
    }

    @Test
    public void whenDeleteTag_thenReturnTrue() throws ServiceException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(1);
        giftCertificate.setName("Tourism");
        giftCertificate.setDescription("Description");
        giftCertificate.setPrice(10);
        giftCertificate.setCreateDate(ZonedDateTime.now());
        giftCertificate.setLastUpdateDate(ZonedDateTime.now());
        giftCertificate.setDuration(10);
        Tag tag = new Tag("spa");
        giftCertificate.getTags().add(tag);

        Mockito.when(giftCertificateDAO.deleteCertificateTagRelation(
                giftCertificate.getId(), tag.getId())).thenReturn(true);
        Mockito.when(giftCertificateDAO.deleteGiftCertificate(giftCertificate.getId())).thenReturn(true);
        boolean result = giftCertificateService.deleteGiftCertificate(giftCertificate);

        Assert.assertTrue(result);
        Mockito.verify(giftCertificateDAO).deleteCertificateTagRelation(giftCertificate.getId(), tag.getId());
        Mockito.verify(giftCertificateDAO).deleteGiftCertificate(giftCertificate.getId());
    }
}
