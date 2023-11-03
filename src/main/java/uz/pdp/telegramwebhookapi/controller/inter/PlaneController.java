package uz.pdp.telegramwebhookapi.controller.inter;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Entity.enums.State;

public interface PlaneController {
    void statePlane(Message message);

    void statePlaneExtra(Message message, State state, String data, String inlineMessageId);
}
