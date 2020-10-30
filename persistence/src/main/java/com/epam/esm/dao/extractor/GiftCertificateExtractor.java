package com.epam.esm.dao.extractor;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException {
        return com.epam.esm.dao.util.GiftCertificateExtractor.extractGiftCertificates(rs);
    }
}
