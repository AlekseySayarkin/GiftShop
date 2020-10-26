package com.epam.esm.dao.extractor;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class GiftCertificateExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(ResultSet rs) throws SQLException {
        return new ArrayList<>(extractDataFromResultSet(rs).values());
    }

    private Map<Integer, GiftCertificate> extractDataFromResultSet(ResultSet rs) throws SQLException {
        Map<Integer, GiftCertificate> map = new LinkedHashMap<>();

        while (rs.next()) {
            GiftCertificate giftCertificate = map.get(rs.getInt(1));
            if (giftCertificate == null) {
                giftCertificate = initGiftCertificate(rs);
                map.put(giftCertificate.getId(), giftCertificate);
            }

            initTags(rs, giftCertificate);
        }

        return map;
    }

    private GiftCertificate initGiftCertificate(ResultSet rs) throws SQLException {
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

        return giftCertificate;
    }

    private void initTags(ResultSet rs, GiftCertificate giftCertificate) throws SQLException {
        Tag tag = new Tag(rs.getInt(10), rs.getString(11));
        if (tag.getId() != 0 && tag.getName() != null) {
            giftCertificate.getTags().add(tag);
        }
    }
}
