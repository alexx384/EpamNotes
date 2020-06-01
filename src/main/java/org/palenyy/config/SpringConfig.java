package org.palenyy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@ComponentScan("org.palenyy")
@Import({DatabaseConfig.class, LocaleConfig.class, ThymeleafConfig.class})
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {
    private final ThymeleafConfig thymeleafConfig;
    private final LocaleConfig localeConfig;

    @Autowired
    public SpringConfig(ThymeleafConfig thymeleafConfig, LocaleConfig localeConfig) {
        this.thymeleafConfig = thymeleafConfig;
        this.localeConfig = localeConfig;
    }

    @Bean(name="multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multi = new CommonsMultipartResolver();
        multi.setMaxUploadSize(100000);

        return multi;
    }

    @Override
    @Description("In order to take effect LocaleChangeInterceptor needs to be added to the application's " +
            "interceptor registry.")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeConfig.localeChangeInterceptor());
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(thymeleafConfig.templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        registry.viewResolver(resolver);
    }
}
