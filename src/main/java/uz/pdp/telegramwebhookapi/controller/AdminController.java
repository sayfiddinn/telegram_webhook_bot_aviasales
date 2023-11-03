package uz.pdp.telegramwebhookapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.controller.inter.AerodromeController;
import uz.pdp.telegramwebhookapi.controller.inter.PlaneController;
import uz.pdp.telegramwebhookapi.repositary.UserRepositary;
import uz.pdp.telegramwebhookapi.service.AdminService;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final PlaneController planeController;
    private final AerodromeController aerodromeController;
    private final UserRepositary userRepositary;
    private final BaseService baseService;

    public void run(Message message, String data, String inlineMessageId) {
        if (message.hasText()) {
            if (message.getText().equals("/start")) {
                baseService.getMainMenu(message.getChatId());
                return;
            }
            State state = userRepositary.findById(message.getChatId()).orElseThrow().getState();
            switch (state){
                case ADM_MAIN_STATE ->
                        adminService.stateMain(message);
                case ADM_PLANE_STATE ->
                        planeController.statePlane(message);
                case ADM_AERDROM_STATE ->
                        aerodromeController.stateAerodrome(message);
                case ADM_AERODROM_ADD_STATE, ADM_AERODROM_SHOW_STATE,
                        ADM_AERODROM_DELETE_STATE,ADM_AERODROM_EDITE_STATE,
                        EXTRA_AERO_NAME_STATE,EXTRA_AERO_EDIT_ALL ->
                        aerodromeController.stateAerodromeExtra(message,state,data,inlineMessageId);
                case ADM_PLANE_ADD_STATE,ADM_PLANE_SHOW_STATE,ADM_PLANE_DELETE_STATE,
                        ADM_PLANE_EDITE_STATE,EXTRA_PLANE_EDIT_ALL,EXTRA_PLANE_EDIT_NAME,
                        EXTRA_PLANE_EDIT_COMPANY,EXTRA_PLANE_EDIT_NUMBER,EXTRA_PLANE_EDIT_SEATS->
                        planeController.statePlaneExtra(message,state,data,inlineMessageId);
            }
        }

    }
}
