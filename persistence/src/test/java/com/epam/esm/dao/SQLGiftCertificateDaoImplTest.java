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
import java.time.format.DateTimeFormatter;
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

    private List<GiftCertificate> initCertificates(int size) {
        List<GiftCertificate> given = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            given.add(certificate);
        }

        return given;
    }

    private GiftCertificate initCertificate() {
        GiftCertificate certificate = new GiftCertificate();
        certificate.setName("Tour to Greece");
        certificate.setDescription("Certificate description");
        certificate.setPrice(99.99);

        certificate.setCreateDate(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC));
        certificate.setLastUpdateDate(certificate.getCreateDate());
        certificate.setDuration(10);

        Set<Tag> tags = new HashSet<>();
        tags.add(tagDao.getTag("tourism"));
        tags.add(tagDao.getTag("relax"));
        certificate.setTags(tags);

        return certificate;
    }

    private void assertEqualsWithoutUpdateDate(GiftCertificate actual, GiftCertificate expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getPrice(), expected.getPrice(), 0.0001);
        Assert.assertEquals(actual.getDuration(), expected.getDuration());
        Assert.assertEquals(actual.getTags(), expected.getTags());
        Assert.assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
    }

    private void assertEquals(List<GiftCertificate> expected, List<GiftCertificate> actual) {
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private void assertEquals(GiftCertificate actual, GiftCertificate expected) {
        Assert.assertEquals(actual.getId(), expected.getId());
        Assert.assertEquals(actual.getName(), expected.getName());
        Assert.assertEquals(actual.getPrice(), expected.getPrice(), 0.0001);
        Assert.assertEquals(actual.getDuration(), expected.getDuration());
        Assert.assertEquals(actual.getTags(), expected.getTags());

        Assert.assertTrue(equalDates(actual.getCreateDate(), expected.getCreateDate()));
        Assert.assertTrue(equalDates(actual.getLastUpdateDate(), expected.getLastUpdateDate()));
    }

    private boolean equalDates(ZonedDateTime first, ZonedDateTime second) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String firstDate = ZonedDateTime.ofInstant(first.toInstant(), ZoneOffset.of("-06:00")).format(formatter);
        String secondDate = ZonedDateTime.ofInstant(second.toInstant(), ZoneOffset.UTC).format(formatter);

        return firstDate.equals(secondDate);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnItById() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getId());
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnItByName() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getName());
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemByTagName() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getGiftCertificateByTagName("relax");
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyDeleteIt() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        for (Tag tag: given.getTags()) {
            giftCertificateDAO.deleteCertificateTagRelation(given.getId(), tag.getId());
        }
        boolean actual = giftCertificateDAO.deleteGiftCertificate(given.getId());
        Assert.assertTrue(actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThem() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(20);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificates();
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyUpdateIt() throws PersistenceException {
        GiftCertificate given = initCertificate();

        given.setId(giftCertificateDAO.addGiftCertificate(given));
        given.setName("new name");
        given.setDescription("new description");
        given.setPrice(199.99);
        given.setDuration(99);
        given.getTags().add(tagDao.getTag("spa"));
        for (Tag tag: given.getTags()) {
            giftCertificateDAO.createCertificateTagRelation(given.getId(), tag.getId());
        }

        giftCertificateDAO.updateGiftCertificate(given);
        GiftCertificate actual = giftCertificateDAO.getGiftCertificate(given.getId());
        assertEqualsWithoutUpdateDate(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByDateAsc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByDate(true);
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByDateDesc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByDate(false);
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameAsc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.addGiftCertificate(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByName(true);
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemSortedByNameDesc() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (int i = 0; i < given.size(); i++) {
            given.get(i).setName(String.valueOf(i));
            given.get(i).setId(giftCertificateDAO.addGiftCertificate(given.get(i)));

            for (Tag tag:  given.get(i).getTags()) {
                giftCertificateDAO.createCertificateTagRelation(given.get(i).getId(), tag.getId());
            }
        }

        Collections.reverse(given);
        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificatesSortedByName(false);
        assertEquals(given, actual);
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnThemByContent() throws PersistenceException {
        List<GiftCertificate> given = initCertificates(10);

        for (GiftCertificate certificate: given) {
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            for (Tag tag: certificate.getTags()) {
                giftCertificateDAO.createCertificateTagRelation(certificate.getId(), tag.getId());
            }
        }

        List<GiftCertificate> actual = giftCertificateDAO.getAllGiftCertificates("Certificate description");
        assertEquals(given, actual);
    }
}
