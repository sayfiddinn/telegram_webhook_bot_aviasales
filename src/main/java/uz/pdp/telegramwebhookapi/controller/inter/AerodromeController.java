package uz.pdp.telegramwebhookapi.controller.inter;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Entity.enums.State;

public interface AerodromeController  {
    void stateAerodrome(Message message);
    void stateAerodromeExtra(Message message, State state, String data, String inlineMessageId);
}
