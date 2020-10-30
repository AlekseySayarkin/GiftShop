package com.epam.esm.config;

import com.epam.esm.dao.TagDao;
import com.epam.esm.dao.impl.SQLGiftCertificateDaoImpl;
import com.epam.esm.dao.impl.SQLTagDaoImpl;
import com.epam.esm.dao.extractor.GiftCertificateExtractor;
import com.epam.esm.dao.mapper.GiftCertificateMapper;
import com.epam.esm.dao.mapper.TagRowMapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.impl.TagServiceImp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@PropertySource("classpath:db.properties")
public class AppConfig {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public TagRowMapper tagRowMapper() {
        return new TagRowMapper();
    }

    @Bean
    public GiftCertificateExtractor giftCertificateExtractor() {
        return new GiftCertificateExtractor();
    }

    @Bean
    public SQLTagDaoImpl tagDAO(JdbcTemplate jdbcTemplate, RowMapper<Tag> rowMapper) {
        return new SQLTagDaoImpl(jdbcTemplate, rowMapper);
    }

    @Bean
    public RowMapper<GiftCertificate> giftCertificateMapper() {
        return new GiftCertificateMapper();
    }

    @Bean
    public SQLGiftCertificateDaoImpl giftCertificateDao(
            JdbcTemplate jdbcTemplate, ResultSetExtractor<List<GiftCertificate>> giftCertificateExtractor,
            RowMapper<GiftCertificate> mapper) {
        return new SQLGiftCertificateDaoImpl(jdbcTemplate, giftCertificateExtractor, mapper);
    }

    @Bean
    public TagServiceImp tagServiceImp(TagDao tagDao) {
        return new TagServiceImp(tagDao);
    }
}
