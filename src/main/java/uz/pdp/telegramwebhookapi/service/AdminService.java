package uz.pdp.telegramwebhookapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.service.inter.AerodromeService;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;
import uz.pdp.telegramwebhookapi.service.inter.PlaneService;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AerodromeService aerodromeService;
    private final PlaneService planeService;
    private final BaseService baseService;

    public void stateMain(Message message) {
        switch (message.getText()) {
            case Buttons.PLANE -> planeService.getPlaneMenu(message.getChatId());
            case Buttons.AERODROM -> aerodromeService.getAerodromeMenu(message.getChatId());
            default -> baseService.getMainMenu(message.getChatId());
        }
    }

}
