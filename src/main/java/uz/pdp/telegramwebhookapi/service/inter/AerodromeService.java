package uz.pdp.telegramwebhookapi.service.inter;

import org.telegram.telegrambots.meta.api.objects.Message;


public interface AerodromeService {
    void addAerodrome(Message message, String data, String inlineMessageId,boolean isEdite);
    void editAerodrome(Message message, String data, String inlineMessageId);
    void deleteAerodrome(Message message, String data, String inlineMessageId);
    void showAerodrome(Message message, String data, String inlineMessageId);
    void showAerodromeById(Long chatId, String text);
    void getAerodromeMenu(Long chatId);
    void editName(Message message, Long chatId, String text);

}
