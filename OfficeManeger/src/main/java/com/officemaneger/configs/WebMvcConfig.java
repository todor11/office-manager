package com.officemaneger.configs;

import com.officemaneger.configs.interceptors.LogInterceptor;
import com.officemaneger.configs.springConverters.LocalDateConverter;
import com.officemaneger.configs.springConverters.LocalDateStringConverter;
import com.officemaneger.configs.springConverters.LocalTimeConverter;
import com.officemaneger.configs.springConverters.LocalTimeStringConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private LogInterceptor logInterceptor;

    private HandlerInterceptorAdapter handlerInterceptorAdapter;

    private LocaleChangeInterceptor localeChangeInterceptor;

    public WebMvcConfig() {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.localeChangeInterceptor);
        registry.addInterceptor(this.logInterceptor);
        registry.addInterceptor(this.handlerInterceptorAdapter);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new LocalTimeConverter("HH:mm"));
        registry.addConverter(new LocalTimeStringConverter());
        registry.addConverter(new LocalDateConverter("dd.MM.yyyy"));
        registry.addConverter(new LocalDateStringConverter());
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("static/i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver(){
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(new Locale("bg"));
        resolver.setCookieName("lang");
        resolver.setCookieMaxAge(4800);
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");

        return localeChangeInterceptor;
    }

    @Autowired
    public void setLogInterceptor(LogInterceptor logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    @Autowired
    public void setHandlerInterceptorAdapter(HandlerInterceptorAdapter handlerInterceptorAdapter) {
        this.handlerInterceptorAdapter = handlerInterceptorAdapter;
    }

    @Autowired
    public void setLocaleChangeInterceptor(LocaleChangeInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }
}
