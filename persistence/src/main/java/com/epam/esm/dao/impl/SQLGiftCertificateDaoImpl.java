package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDAO;
import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class SQLGiftCertificateDaoImpl implements GiftCertificateDAO {

    //private final JdbcTemplate jdbcTemplate;

    @Override
    public GiftCertificate getGiftCertificate(int id) {
        return null;
    }

    @Override
    public GiftCertificate getGiftCertificate(String name) {
        return null;
    }

    @Override
    public List<GiftCertificate> getAllGiftCertificates() {
        return null;
    }

    @Override
    public int addGiftCertificate(GiftCertificate giftCertificate) {
        return 0;
    }

    @Override
    public boolean deleteGiftCertificate(GiftCertificate giftCertificate) {
        return false;
    }

    @Override
    public boolean updateGiftCertificate(GiftCertificate giftCertificate) {
        return false;
    }
}
