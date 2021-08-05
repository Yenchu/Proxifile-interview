package idv.demo.backend.config;

import idv.demo.backend.web.CorsHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public CorsHandlerInterceptor corsHandlerInterceptor() {

        return new CorsHandlerInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(corsHandlerInterceptor()).addPathPatterns("/**");
    }
}