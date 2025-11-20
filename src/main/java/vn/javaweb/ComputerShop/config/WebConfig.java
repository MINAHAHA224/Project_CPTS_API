package vn.javaweb.ComputerShop.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig  implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("resources/images/product/**")
                .addResourceLocations("file:///D:/Android_FinalProject/images/product/");

        registry.addResourceHandler("resources/images/avatar/**").
                addResourceLocations("file:///D:/Android_FinalProject/images/avatar/");
    }
}
