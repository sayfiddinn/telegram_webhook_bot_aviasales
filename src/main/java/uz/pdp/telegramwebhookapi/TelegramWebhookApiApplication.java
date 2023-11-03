package uz.pdp.telegramwebhookapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "uz.pdp.telegramwebhookapi")
public class TelegramWebhookApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramWebhookApiApplication.class, args);
    }

}
