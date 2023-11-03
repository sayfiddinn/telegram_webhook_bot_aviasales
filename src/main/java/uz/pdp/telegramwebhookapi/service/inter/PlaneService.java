package uz.pdp.telegramwebhookapi.service.inter;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Entity.enums.State;

public interface PlaneService {
    void addPlane(Message message,String data, String inlineMessageId,boolean isEdite);
    void deletePlane(Message message,String data, String inlineMessageId);
    void editePlane(Message message,String data, String inlineMessageId);
    void editPlaneElement(Message message, Long chatId, String text, State state);

    void showPlane(Message message,String data, String inlineMessageId);
    void getPlaneMenu(Long chatId);
}
