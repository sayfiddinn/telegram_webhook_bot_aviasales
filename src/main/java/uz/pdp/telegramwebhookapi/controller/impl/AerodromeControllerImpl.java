package uz.pdp.telegramwebhookapi.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.controller.inter.AerodromeController;
import uz.pdp.telegramwebhookapi.service.inter.AerodromeService;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;

@Controller
@RequiredArgsConstructor
public class AerodromeControllerImpl implements AerodromeController {
    private final AerodromeService aerodromeService;
    private final BaseService baseService;
    @Override
    public void stateAerodrome(Message message) {
        switch (message.getText()) {
            case Buttons.AER_ADD -> {
                baseService.exitState(message.getChatId(), State.ADM_AERODROM_ADD_STATE);
                aerodromeService.addAerodrome(message, null, null,false);
            }
            case Buttons.AER_EDIT -> {
                baseService.exitState(message.getChatId(), State.ADM_AERODROM_EDITE_STATE);
                aerodromeService.editAerodrome(message,null,null);

            }
            case Buttons.AER_DELETE -> {
                baseService.exitState(message.getChatId(), State.ADM_AERODROM_DELETE_STATE);
                aerodromeService.deleteAerodrome(message, null, null);
            }
            case Buttons.AER_SHOW -> {
                baseService.exitState(message.getChatId(), State.ADM_AERODROM_SHOW_STATE);
                aerodromeService.showAerodromeById(message.getChatId(), message.getText());
            }
            case Buttons.AERS_SHOW -> {
                baseService.exitState(message.getChatId(), State.ADM_AERODROM_SHOW_STATE);
                aerodromeService.showAerodrome(message, null, null);
            }
            case Buttons.EXIT -> baseService.getMainMenu(message.getChatId());
        }
    }

    @Override
    public void stateAerodromeExtra(Message message, State state, String data, String inlineMessageId) {
        switch (state){
            case ADM_AERODROM_ADD_STATE -> aerodromeService.addAerodrome(message,data,inlineMessageId,false);
            case ADM_AERODROM_SHOW_STATE -> aerodromeService.showAerodrome(message,data,inlineMessageId);
            case ADM_AERODROM_DELETE_STATE -> aerodromeService.deleteAerodrome(message,data,inlineMessageId);
            case ADM_AERODROM_EDITE_STATE -> aerodromeService.editAerodrome(message,data,inlineMessageId);
            case EXTRA_AERO_NAME_STATE -> aerodromeService.editName(message, message.getChatId(), message.getText());
            case EXTRA_AERO_EDIT_ALL -> aerodromeService.addAerodrome(message,data,inlineMessageId,true);
            default -> baseService.getMainMenu(message.getChatId());
        }
    }
}
