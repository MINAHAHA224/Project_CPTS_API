package vn.javaweb.ComputerShop.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.LocaleResolver; // Quan trọng: import interface
import org.springframework.web.servlet.i18n.SessionLocaleResolver; // Import

import java.util.Locale;

@Slf4j
@Configuration
public class LocaleConfiguration {


    @Bean
    public LocaleResolver localeResolver() {
        log.info(">>>>>>>>>> Creating AcceptHeaderLocaleResolver bean in LocaleConfiguration! <<<<<<<<<<");

        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(new Locale("vi", "VN"));
        return resolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setUseCodeAsDefaultMessage(true);
        source.setCacheSeconds(3600);
        return source;
    }

}
