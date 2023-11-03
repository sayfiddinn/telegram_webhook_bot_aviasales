package uz.pdp.telegramwebhookapi.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {


    @Bean
    public Base64 base64(){
        return new Base64();
    }

}
