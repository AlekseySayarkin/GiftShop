package com.epam.esm.dao;

import com.epam.esm.dao.exception.PersistenceException;
import com.epam.esm.dao.extractor.GiftCertificateExtractor;
import com.epam.esm.dao.impl.SQLGiftCertificateDaoImpl;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

public class SQLGiftCertificateDaoImplTest {

    private GiftCertificateDAO giftCertificateDAO;
    private TagDao tagDao;

    @Before
    public void init() throws PersistenceException {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("db/ClearTables.sql")
                .addScript("db/Tags.sql")
                .addScript("db/GiftCertificates.sql")
                .addScript("db/CertificateDetails.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        tagDao = new SQLTagDaoImpl(jdbcTemplate, new TagRowMapper());
        giftCertificateDAO = new SQLGiftCertificateDaoImpl(
                jdbcTemplate, new GiftCertificateExtractor(), new GiftCertificateMapper());

        tagDao.addTag(new Tag("spa"));
        tagDao.addTag(new Tag("relax"));
        tagDao.addTag(new Tag("tourism"));
    }

    private GiftCertificate initCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Tour to Greece");
        certificate.setDescription("Certificate description");
        certificate.setPrice(99.99);
        certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setLastUpdateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setDuration(10);

        Set<Tag> tags = new HashSet<>();
        tags.add(tagDao.getTag("tourism"));
        tags.add(tagDao.getTag("relax"));
        certificate.setTags(tags);

        return certificate;
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnsIt() throws PersistenceException {
        GiftCertificate expected;
        GiftCertificate actual;

        expected = initCertificate();
        expected.setId(giftCertificateDAO.addGiftCertificate(expected));

        giftCertificateDAO.createCertificateTagRelation(expected.getId(), tagDao.getTag("tourism").getId());
        giftCertificateDAO.createCertificateTagRelation(expected.getId(), tagDao.getTag("relax").getId());

        actual = giftCertificateDAO.getGiftCertificate(expected.getName());
        Assert.assertEquals(expected, actual);

        actual = giftCertificateDAO.getGiftCertificate(expected.getId());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnsItByTagNAme() throws PersistenceException {
        List<GiftCertificate> expected;
        List<GiftCertificate> actual;

        expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();

            certificate.setName("name" + i);
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tagDao.getTag("tourism").getId());
            giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tagDao.getTag("relax").getId());

            expected.add(certificate);
        }

        actual = giftCertificateDAO.getGiftCertificateByTagName("relax");
        Assert.assertEquals(expected, actual);

        actual = giftCertificateDAO.getGiftCertificateByTagName("spa");
        Assert.assertNotEquals(expected, actual);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyDeletesIt() throws PersistenceException {
        GiftCertificate certificate = initCertificate();
        certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

        for (Tag tag: certificate.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tagDao.getTag(tag.getName()).getId());
        }

        for (Tag tag: certificate.getTags()) {
            giftCertificateDAO.deleteCertificateTagRelation(certificate.getId(), tagDao.getTag(tag.getName()).getId());
        }

        Assert.assertTrue(giftCertificateDAO.deleteGiftCertificate(certificate.getId()));
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnsThemAll() throws PersistenceException {
        List<GiftCertificate> expected;
        List<GiftCertificate> actual;

        expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setName("name" + i);
            certificate.setDescription("description" + i);
            certificate.setPrice(10 * i + 1);
            certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            certificate.setLastUpdateDate( ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
            certificate.setDuration(i);
            certificate.setTags(new HashSet<>(tagDao.getAllTags()));
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }

            expected.add(certificate);
        }

        actual = giftCertificateDAO.getAllGiftCertificates();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void correctlyUpdateGiftCertificate() throws PersistenceException {
        GiftCertificate toUpdate;
        GiftCertificate updated;

        toUpdate = initCertificate();
        toUpdate.setId(giftCertificateDAO.addGiftCertificate(toUpdate));

        toUpdate.setName("new name");
        toUpdate.setDescription("new description");
        toUpdate.setPrice(199.99);
        toUpdate.setDuration(99);
        toUpdate.getTags().add(tagDao.getTag("spa"));

        for (Tag tag: toUpdate.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(toUpdate.getId(), tag.getId());
        }

        boolean result = giftCertificateDAO.updateGiftCertificate(toUpdate);
        Assert.assertTrue(result);

        updated = giftCertificateDAO.getGiftCertificate(toUpdate.getId());

        Assert.assertEquals(toUpdate.getId(), updated.getId());
        Assert.assertEquals(toUpdate.getName(), updated.getName());
        Assert.assertEquals(toUpdate.getPrice(), updated.getPrice(), 0.0001);
        Assert.assertEquals(toUpdate.getDuration(), updated.getDuration());
        Assert.assertEquals(toUpdate.getTags(), updated.getTags());
    }

    @Test
    public void correctlyReturnSortedByDateCertificates() throws PersistenceException {
        List<GiftCertificate> expectedCertificateList;
        List<GiftCertificate> actualCertificateList;

        expectedCertificateList = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + i);

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }

            expectedCertificateList.add(certificate);
        }

        actualCertificateList = giftCertificateDAO.getAllGiftCertificatesSortedByDate(true);
        Assert.assertEquals(expectedCertificateList, actualCertificateList);

        actualCertificateList = giftCertificateDAO.getAllGiftCertificatesSortedByDate(false);
        Assert.assertNotEquals(expectedCertificateList, actualCertificateList);

        Collections.reverse(expectedCertificateList);
        actualCertificateList = giftCertificateDAO.getAllGiftCertificatesSortedByDate(false);
        Assert.assertEquals(expectedCertificateList, actualCertificateList);
    }

    @Test
    public void correctlyReturnSortedByNameCertificates() throws PersistenceException {
        List<GiftCertificate> expectedGiftCertificates;
        List<GiftCertificate> actualCertificateList;

        expectedGiftCertificates = new ArrayList<>();
        for (int i = 96; i <= 122; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + (char) i);

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }

            expectedGiftCertificates.add(certificate);
        }

        actualCertificateList = giftCertificateDAO.getAllGiftCertificatesSortedByName(true);
        Assert.assertEquals(expectedGiftCertificates,actualCertificateList);

        actualCertificateList =  giftCertificateDAO.getAllGiftCertificatesSortedByName(false);
        Assert.assertNotEquals(
                expectedGiftCertificates, actualCertificateList);

        Collections.reverse(expectedGiftCertificates);
        actualCertificateList = giftCertificateDAO.getAllGiftCertificatesSortedByName(false);
        Assert.assertEquals(expectedGiftCertificates, actualCertificateList);
    }

    @Test
    public void correctlyReturnCertificatesByContent() throws PersistenceException {
        GiftCertificate expected;

        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + (char) i + " Additional Info");

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }

            expected = giftCertificateDAO.getAllGiftCertificates("Certificate" + (char) i).get(0);
            Assert.assertEquals(certificate, expected);

            int size = giftCertificateDAO.getAllGiftCertificates("New content" + (char) i).size();
            Assert.assertEquals(0, size);
        }

        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setDescription("Description" + (char) i + " Additional Info");

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }

            expected = giftCertificateDAO.getAllGiftCertificates("Description" + (char) i).get(0);
            Assert.assertEquals(certificate, expected);

            int size = giftCertificateDAO.getAllGiftCertificates("New content" + (char) i).size();
            Assert.assertEquals(0, size);
        }
    }
}
