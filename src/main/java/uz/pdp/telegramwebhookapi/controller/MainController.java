package uz.pdp.telegramwebhookapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Entity.ChatUser;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.pdp.telegramwebhookapi.repositary.UserRepositary;
import uz.pdp.telegramwebhookapi.util.Utils;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final UserRepositary userRepositary;
    private final AdminController adminController;


    public void run(Update update) {

        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();
            if (Utils.adminList.contains(chatId)) {
                adminController.run(update.getMessage(), null, null);
            } else {
                userRepositary.save(new ChatUser(chatId, State.NEW));
            }
        } else if (update.hasCallbackQuery()) {
            Message message = update.getCallbackQuery().getMessage();
            String inlineMessageId = update.getCallbackQuery().getInlineMessageId();
            String data = update.getCallbackQuery().getData();
            if (Utils.adminList.contains(message.getChatId())) {
                adminController.run(message, data, inlineMessageId);
            } else {
                userRepositary.save(new ChatUser(message.getChatId(), State.NEW));
            }
        }

    }

}
