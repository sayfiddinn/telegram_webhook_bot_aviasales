package uz.pdp.telegramwebhookapi.KeyBoardWork;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class GetReplyMarkUp {

    public static ReplyKeyboardMarkup getKeyboard(String[][] s) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        List<KeyboardRow> list = new ArrayList<>();
        for (String[] strings : s) {
            KeyboardRow row = new KeyboardRow();
            for (String string : strings) {
                if (string!=null) {
                    KeyboardButton button = new KeyboardButton();
                    button.setText(string);
                    row.add(button);
                }
            }
            list.add(row);
        }
        markup.setKeyboard(list);
        markup.setResizeKeyboard(true);
        return markup;
    }

    public static InlineKeyboardMarkup getInlineKeyboard(String[][] s) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> collection = new ArrayList<>();
        for (String[] button : s) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (String item : button) {
                if (item == null) continue;
                String[] value = item.split(":");
                row.add(button(value[0], value[1]));
            }
            collection.add(row);
        }
        markup.setKeyboard(collection);
        return markup;
    }

    public static InlineKeyboardButton button(String text, String data) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(data);
        return button;
    }
}
