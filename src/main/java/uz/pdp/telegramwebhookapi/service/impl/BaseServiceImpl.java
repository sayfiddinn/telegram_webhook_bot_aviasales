package uz.pdp.telegramwebhookapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.Entity.ChatUser;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.KeyBoardWork.GetReplyMarkUp;
import uz.pdp.telegramwebhookapi.feign.TelegramFeign;
import uz.pdp.telegramwebhookapi.payload.Country;
import uz.pdp.telegramwebhookapi.payload.InlineDTO;
import uz.pdp.telegramwebhookapi.repositary.UserRepositary;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;
import uz.pdp.telegramwebhookapi.util.Utils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BaseServiceImpl implements BaseService {
    private final TelegramFeign telegramFeign;
    private final UserRepositary userRepositary;
    @Autowired
    @Lazy
    Base64 base64;

    @Override
    public void getMenu(Long chatId, String text, String[][] menu) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(GetReplyMarkUp.getKeyboard(menu));
        sendMessage.setParseMode(ParseMode.HTML);
        telegramFeign.sendMessageToUser(Utils.token, sendMessage);
    }

    @Override
    public void exitState(Long chatId, State state) {
        ChatUser user = userRepositary.findById(chatId).orElseThrow();
        user.setState(state);
        userRepositary.save(user);
    }

    @Override
    public void send(SendMessage sendMessage) {
        telegramFeign.sendMessageToUser(Utils.token, sendMessage);
    }

    @Override
    public void getMainMenu(Long chatId) {
        getMenu(chatId, "main menu", Utils.ADMIN_MAIN_MENU);
        exitState(chatId, State.ADM_MAIN_STATE);
    }

    @Override
    public void deleteMarkup(Long chatId, Integer messageId, String inlineMessageId) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setInlineMessageId(inlineMessageId);
        editMessageReplyMarkup.setReplyMarkup(null);
        telegramFeign.deleteInlineMarkup(Utils.token, editMessageReplyMarkup);
    }

    @Override
    public String[][] getArray(List<InlineDTO> list, String suffixOne, String suffixTwo, Boolean isInline) {
        String[][] arr = new String[(list.size() / 2) + 1][2];
        int j = 0;
        for (int i = 0; i < list.size() - 1; i += 2) {
            arr[j][0] = suffixOne + list.get(i).getName();
            arr[j][1] = suffixTwo + list.get(i + 1).getName();
            if (isInline) {
                arr[j][0] += ":" + list.get(i).getId();
                arr[j][1] += ":" + list.get(i + 1).getId();
            }
            j++;
        }
        if (list.size() % 2 == 1) {
            arr[j][0] = suffixOne + list.get(list.size() - 1).getName();
            if (isInline) {
                arr[j][0] += ":" + list.get(list.size() - 1).getId();
            }
        }
        arr[j][1] = isInline ? Buttons.X + ":x" : Buttons.EXIT;
        return arr;
    }

    @Override
    public String[][] getArrayCountry(List<Country> list, String suffixOne, String suffixTwo, Boolean isInline) {
        String[][] arr = new String[(list.size() / 2) + 1][2];
        int j = 0;
        for (int i = 0; i < list.size() - 1; i += 2) {
            arr[j][0] = suffixOne + list.get(i).getName();
            arr[j][1] = suffixTwo + list.get(i + 1).getName();
            if (isInline) {
                arr[j][0] += ":" + list.get(i).getId();
                arr[j][1] += ":" + list.get(i + 1).getId();
            }
            j++;
        }
        if (list.size() % 2 == 1) {
            arr[j][0] = suffixOne + list.get(list.size() - 1).getName();
            if (isInline) {
                arr[j][0] += ":" + list.get(list.size() - 1).getId();
            }
        }
        arr[j][1] = isInline ? Buttons.X + ":x" : Buttons.EXIT;
        return arr;
    }



    @Override
    public String getAuthentication() {
        String s = "admin@gmail.com:root123";
        return Utils.BASIC+new String(base64.encode(s.getBytes()));
    }
}
