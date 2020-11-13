package com.epam.esm.config;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.ExceptionHandlerController;
import com.epam.esm.controller.TagController;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.epam.esm.controller"})
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CookieLocaleResolver localeResolver() {
        var resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setCookieName("lang");
        return resolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("lang/message");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }

    @Bean
    public TagController tagController(
            TagService tagService, CookieLocaleResolver cookieLocaleResolver, MessageSource messageSource) {
        return new TagController(tagService, cookieLocaleResolver, messageSource);
    }

    @Bean
    public CertificateController certificateController(
            MessageSource messageSource, CookieLocaleResolver cookieLocaleResolver,
            GiftCertificateService giftCertificateService) {
        return new CertificateController(messageSource, cookieLocaleResolver, giftCertificateService);
    }

    @Bean
    public ExceptionHandlerController exceptionHandlerController() {
        return new ExceptionHandlerController();
    }
}
