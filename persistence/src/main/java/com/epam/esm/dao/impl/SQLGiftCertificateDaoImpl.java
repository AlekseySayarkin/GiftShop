package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Set;

@Repository
public class SQLGiftCertificateDaoImpl implements GiftCertificateDAO {

    private static final String SQL_GET_CERTIFICATE_BY_ID =
            "select * from GiftCertificates " +
            "inner join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "and GiftCertificates.ID = ? " +
            "inner join Tags " +
            "on Tags.ID = CertificateDetails.TagID";

    private static final String SQL_GET_CERTIFICATE_BY_NAME =
            "select * from GiftCertificates " +
            "inner join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "and GiftCertificates.Name = ? " +
            "inner join Tags " +
            "on Tags.ID = CertificateDetails.TagID";

    private static final String SQL_GET_ALL_CERTIFICATES =
            "select * from GiftCertificates " +
            "inner join CertificateDetails " +
            "on GiftCertificates.ID = CertificateDetails.CertificateID " +
            "inner join Tags " +
            "on Tags.ID = CertificateDetails.TagID";

    private static final String SQL_ADD_CERTIFICATE =
            "insert into GiftCertificates " +
            "(Name, Description, Price, CreateDate, LastUpdateDate, Duration) " +
            "values (?, ?, ?, ?, ?, ?)";

    private static final String SQL_JOIN_TAG =
            "insert into CertificateDetails (TagID, CertificateID) values (?, ?)";

    private static final String SQL_DELETE_CERTIFICATE =
            "delete from GiftCertificates where (ID = ?)";

    private static final String SQL_DELETE_JOIN =
            "delete from CertificateDetails where (CertificateID = ?)";

    private final JdbcTemplate jdbcTemplate;
    private final ResultSetExtractor<List<GiftCertificate>> extractor;

    @Autowired
    private TagDao tagDao;

    @Autowired
    public SQLGiftCertificateDaoImpl(
            JdbcTemplate jdbcTemplate, ResultSetExtractor<List<GiftCertificate>> extractor) {
        this.jdbcTemplate = jdbcTemplate;
        this.extractor = extractor;
    }

    @Override
    public GiftCertificate getGiftCertificate(String name) {
        List<GiftCertificate> result = jdbcTemplate.query(SQL_GET_CERTIFICATE_BY_NAME, extractor, name);

        if (result == null || result.isEmpty()) {
            return null; //todo ask whether returning null is ok
        }

        return result.get(0);
    }

    @Override
    public GiftCertificate getGiftCertificate(int id) {
        List<GiftCertificate> result = jdbcTemplate.query(SQL_GET_CERTIFICATE_BY_ID, extractor, id);

        if (result == null || result.isEmpty()) {
            return null; //todo ask whether returning null is ok
        }

        return result.get(0);
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return jdbcTemplate.query(SQL_GET_ALL_CERTIFICATES, extractor);
    }

    @Override
    public int addGiftCertificate(GiftCertificate giftCertificate) {
        int id = addGiftCertificateToDB(giftCertificate);
        if (id == 0) {
            return 0;
        }

        addTagsToDB(giftCertificate.getTags());

        for (Tag tag : giftCertificate.getTags()) {
            jdbcTemplate.update(SQL_JOIN_TAG, tag.getId(), id);
        }

        return id;
    }

    private int addGiftCertificateToDB(GiftCertificate giftCertificate) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_ADD_CERTIFICATE, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setDouble(3, giftCertificate.getPrice());
            ps.setTimestamp(4, Timestamp.from(giftCertificate.getCreateDate().toInstant()));
            ps.setTimestamp(5, Timestamp.from(giftCertificate.getLastUpdateDate().toInstant()));
            ps.setInt(6, giftCertificate.getDuration());
            return ps;
        }, holder);

        return holder.getKey() == null ? 0 : holder.getKey().intValue();
    }

    private void addTagsToDB(Set<Tag> tags) {
        List<Tag> existingTags = tagDao.getAllTags();

        for (Tag tag : tags) {
            if (!existingTags.contains(tag)) {
                tag.setId(tagDao.addTag(tag));
            }
        }
    }

    @Override
    public boolean deleteGiftCertificate(GiftCertificate giftCertificate) {
        jdbcTemplate.update(SQL_DELETE_JOIN, giftCertificate.getId());

        return jdbcTemplate.update(SQL_DELETE_CERTIFICATE, giftCertificate.getId()) == 1;
    }

    @Override
    public boolean updateGiftCertificate(GiftCertificate giftCertificate) {
        return false;
    }
}
