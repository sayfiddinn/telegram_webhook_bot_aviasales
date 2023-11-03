package uz.pdp.telegramwebhookapi.service.inter;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.payload.Country;
import uz.pdp.telegramwebhookapi.payload.InlineDTO;

import java.util.List;

public interface BaseService {
    void getMenu(Long chatId, String text, String[][] menu);
    void exitState(Long chatId, State state);
    void send(SendMessage sendMessage);
    void getMainMenu(Long chatId);
    void deleteMarkup(Long chatId, Integer messageId, String inlineMessageId);
    String[][] getArray(List<InlineDTO> list, String suffixOne, String suffixTwo, Boolean isInline);
    String[][] getArrayCountry(List<Country> list, String suffixOne, String suffixTwo, Boolean isInline);
    String getAuthentication();
}
