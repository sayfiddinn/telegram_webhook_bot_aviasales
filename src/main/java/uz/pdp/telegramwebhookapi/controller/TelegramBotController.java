package uz.pdp.telegramwebhookapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.telegramwebhookapi.service.TelegramBotService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/telegram")
public class TelegramBotController {
    final TelegramBotService telegramBotService;

    @PostMapping
    void getUpdate(@RequestBody Update update) {
        telegramBotService.getUpdates(update);
    }

}