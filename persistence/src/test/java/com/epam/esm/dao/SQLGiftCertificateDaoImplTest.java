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
        tags.add(tagDao.getTagByName("tourism"));
        tags.add(tagDao.getTagByName("relax"));
        certificate.setTags(tags);

        return certificate;
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnsIt() throws PersistenceException {
        GiftCertificate added = initCertificate();
        added.setId(giftCertificateDAO.addGiftCertificate(added));

        giftCertificateDAO.createJoin(added.getId(), tagDao.getTagByName("tourism").getId());
        giftCertificateDAO.createJoin(added.getId(), tagDao.getTagByName("relax").getId());

        Assert.assertEquals(added, giftCertificateDAO.getGiftCertificate(added.getName()));
        Assert.assertEquals(added, giftCertificateDAO.getGiftCertificate(added.getId()));
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyReturnsItByTagNAme() throws PersistenceException {
        List<GiftCertificate> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("name" + i);

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("tourism").getId());
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("relax").getId());

            list.add(certificate);
        }

        Assert.assertEquals(list, giftCertificateDAO.getGiftCertificateByTagName("relax"));
        Assert.assertNotEquals(list, giftCertificateDAO.getGiftCertificateByTagName("spa"));
    }

    @Test
    public void whenAddGiftCertificate_thenCorrectlyDeletesIt() throws PersistenceException {
        GiftCertificate added = initCertificate();

        added.setId(giftCertificateDAO.addGiftCertificate(added));

        giftCertificateDAO.createJoin(added.getId(), tagDao.getTagByName("tourism").getId());
        giftCertificateDAO.createJoin(added.getId(), tagDao.getTagByName("relax").getId());

        giftCertificateDAO.deleteJoin(added.getId(), tagDao.getTagByName("tourism").getId());
        giftCertificateDAO.deleteJoin(added.getId(), tagDao.getTagByName("relax").getId());

        Assert.assertTrue(giftCertificateDAO.deleteGiftCertificate(added));
    }

    @Test
    public void whenAddGiftCertificates_thenCorrectlyReturnsThemAll() throws PersistenceException {
        List<GiftCertificate> certificates = new ArrayList<>();

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
                giftCertificateDAO.createJoin(certificate.getId(), tag.getId());
            }

            certificates.add(certificate);
        }

        Assert.assertEquals(certificates, giftCertificateDAO.getAllGiftCertificates());
    }

    @Test
    public void correctlyUpdateGiftCertificate() throws PersistenceException {
        GiftCertificate toUpdate = initCertificate();
        toUpdate.setId(giftCertificateDAO.addGiftCertificate(toUpdate));

        toUpdate.setName("new name");
        toUpdate.setDescription("new description");
        toUpdate.setPrice(199.99);
        toUpdate.setDuration(99);
        toUpdate.getTags().add(tagDao.getTagByName("spa"));

        giftCertificateDAO.createJoin(toUpdate.getId(), tagDao.getTagByName("tourism").getId());
        giftCertificateDAO.createJoin(toUpdate.getId(), tagDao.getTagByName("relax").getId());
        giftCertificateDAO.createJoin(toUpdate.getId(), tagDao.getTagByName("spa").getId());

        Assert.assertTrue(giftCertificateDAO.updateGiftCertificate(toUpdate));
        GiftCertificate updated = giftCertificateDAO.getGiftCertificate(toUpdate.getId());

        Assert.assertEquals(toUpdate.getId(), updated.getId());
        Assert.assertEquals(toUpdate.getName(), updated.getName());
        Assert.assertEquals(toUpdate.getPrice(), updated.getPrice(), 0);
        Assert.assertEquals(toUpdate.getDuration(), updated.getDuration());
        Assert.assertEquals(toUpdate.getTags(), updated.getTags());
    }

    @Test
    public void correctlyReturnSortedByDateCertificates() throws PersistenceException {
        List<GiftCertificate> giftCertificates = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + i);
            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("tourism").getId());
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("relax").getId());
            giftCertificates.add(certificate);
        }

        Assert.assertEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByDate(true));
        Assert.assertNotEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByDate(false));

        Collections.reverse(giftCertificates);
        Assert.assertEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByDate(false));
    }

    @Test
    public void correctlyReturnSortedByNameCertificates() throws PersistenceException {
        List<GiftCertificate> giftCertificates = new ArrayList<>();

        for (int i = 96; i <= 122; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + (char) i);

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));

            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("tourism").getId());
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("relax").getId());

            giftCertificates.add(certificate);
        }

        Assert.assertEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByName(true));
        Assert.assertNotEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByName(false));

        Collections.reverse(giftCertificates);
        Assert.assertEquals(
                giftCertificates, giftCertificateDAO.getAllGiftCertificatesSortedByName(false));
    }

    @Test
    public void correctlyReturnCertificatesByContent() throws PersistenceException {
        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setName("Certificate" + (char) i + " Additional Info");


            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("tourism").getId());
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("relax").getId());

            Assert.assertEquals(certificate,
                    giftCertificateDAO.getAllGiftCertificates("Certificate" + (char) i).get(0));
            Assert.assertEquals(0,
                    giftCertificateDAO.getAllGiftCertificates("New content" + (char) i).size());
        }

        for (int i = 0; i < 10; i++) {
            GiftCertificate certificate = initCertificate();
            certificate.setDescription("Description" + (char) i + " Additional Info");

            certificate.setId(giftCertificateDAO.addGiftCertificate(certificate));
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("tourism").getId());
            giftCertificateDAO.createJoin(certificate.getId(), tagDao.getTagByName("relax").getId());

            Assert.assertEquals(certificate,
                    giftCertificateDAO.getAllGiftCertificates("Description" + (char) i).get(0));
            Assert.assertEquals(0,
                    giftCertificateDAO.getAllGiftCertificates("New content" + (char) i).size());
        }
    }
}
