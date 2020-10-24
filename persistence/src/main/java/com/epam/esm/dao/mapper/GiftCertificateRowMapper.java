package com.epam.esm.dao.mapper;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;

@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();

        giftCertificate.setId(rs.getInt(1));
        giftCertificate.setName(rs.getString(2));
        giftCertificate.setDescription(rs.getString(3));
        giftCertificate.setPrice(rs.getDouble(4));
        giftCertificate.setCreateDate(
                ZonedDateTime.ofInstant(
                        ((Timestamp) rs.getObject(5)).toInstant(),
                        ZoneOffset.UTC
                )
        );
        giftCertificate.setLastUpdateDate(
                ZonedDateTime.ofInstant(
                        ((Timestamp) rs.getObject(6)).toInstant(),
                        ZoneOffset.UTC
                )
        );
        giftCertificate.setDuration(rs.getInt(7));
        //giftCertificate.setTags((Set<Tag>) rs.getObject(8));
        return giftCertificate;
    }
}
