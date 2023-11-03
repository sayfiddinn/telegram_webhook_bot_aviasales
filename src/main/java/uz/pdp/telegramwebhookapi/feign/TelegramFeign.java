package uz.pdp.telegramwebhookapi.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import uz.pdp.telegramwebhookapi.util.Utils;

@Service
@FeignClient(url = Utils.TELEGRAM_BASE_URL_WITHOUT_BOT,name = "Feign")
public interface TelegramFeign {

    @PostMapping("{path}/sendMessage")
    ResponseEntity<?> sendMessageToUser(@RequestParam String path, @RequestBody SendMessage sendMessage);
    @PostMapping("{path}/editMessageReplyMarkup")
    ResponseEntity<?> deleteInlineMarkup(@RequestParam String path, @RequestBody EditMessageReplyMarkup editMessageReplyMarkup);

}
