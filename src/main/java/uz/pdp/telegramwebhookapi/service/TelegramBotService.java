package uz.pdp.telegramwebhookapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.telegramwebhookapi.controller.MainController;


@Service
@RequiredArgsConstructor
public class TelegramBotService {
    final MainController maincontroller;
    public void getUpdates(Update update){
        if(update==null)return;
        maincontroller.run(update);
    }
}
