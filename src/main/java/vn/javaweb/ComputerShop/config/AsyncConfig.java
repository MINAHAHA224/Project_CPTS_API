package vn.javaweb.ComputerShop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskMailExecutor")
    public Executor taskMailExecutor (){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(3);
        threadPoolTaskExecutor.setMaxPoolSize(5);
        threadPoolTaskExecutor.setQueueCapacity(100); // if full 100 task per base core ( 3 ) -> use max core (5)
        threadPoolTaskExecutor.setThreadNamePrefix("AsyncMail-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}
