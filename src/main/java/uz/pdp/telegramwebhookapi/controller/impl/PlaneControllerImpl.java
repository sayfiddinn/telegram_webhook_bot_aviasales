package uz.pdp.telegramwebhookapi.controller.impl;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.est.CACertsResponse;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.controller.inter.PlaneController;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;
import uz.pdp.telegramwebhookapi.service.inter.PlaneService;

@Controller
@RequiredArgsConstructor
public class PlaneControllerImpl implements PlaneController {
    private final PlaneService planeService;
    private final BaseService baseService;


    @Override
    public void statePlane(Message message) {
        switch (message.getText()) {
            case Buttons.PLANE_ADD -> {
                baseService.exitState(message.getChatId(), State.ADM_PLANE_ADD_STATE);
                planeService.addPlane(message, null, null, false);
            }
            case Buttons.PLANE_DELETE -> {
                baseService.exitState(message.getChatId(), State.ADM_PLANE_DELETE_STATE);
                planeService.deletePlane(message,null,null);
            }
            case Buttons.PLANES_SHOW -> {
                baseService.exitState(message.getChatId(), State.ADM_PLANE_SHOW_STATE);
                planeService.showPlane(message, null, null);
            }
            case Buttons.PLANE_EDIT -> {
                baseService.exitState(message.getChatId(), State.ADM_PLANE_EDITE_STATE);
                planeService.editePlane(message, null, null);
            }
            case Buttons.EXIT -> baseService.getMainMenu(message.getChatId());
            default -> planeService.getPlaneMenu(message.getChatId());
        }
    }

    @Override
    public void statePlaneExtra(Message message, State state, String data, String inlineMessageId) {
        switch (state){
            case ADM_PLANE_ADD_STATE -> planeService.addPlane(message,data,inlineMessageId,false);
            case ADM_PLANE_SHOW_STATE -> planeService.showPlane(message,data,inlineMessageId);
            case ADM_PLANE_DELETE_STATE -> planeService.deletePlane(message,data,inlineMessageId);
            case ADM_PLANE_EDITE_STATE -> planeService.editePlane(message,data,inlineMessageId);
            case EXTRA_PLANE_EDIT_ALL -> planeService.addPlane(message,data,inlineMessageId,true);
            case EXTRA_PLANE_EDIT_NAME, EXTRA_PLANE_EDIT_COMPANY,
                    EXTRA_PLANE_EDIT_NUMBER, EXTRA_PLANE_EDIT_SEATS -> planeService
                    .editPlaneElement(message, message.getChatId(), message.getText(),state);
            default -> baseService.getMainMenu(message.getChatId());
        }
    }
}
