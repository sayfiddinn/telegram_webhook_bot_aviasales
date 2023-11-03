package uz.pdp.telegramwebhookapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.Entity.enums.State;
import uz.pdp.telegramwebhookapi.KeyBoardWork.GetReplyMarkUp;
import uz.pdp.telegramwebhookapi.feign.AviaSalesFeign;
import uz.pdp.telegramwebhookapi.feign.TelegramFeign;
import uz.pdp.telegramwebhookapi.payload.Aerodrome;
import uz.pdp.telegramwebhookapi.payload.Country;
import uz.pdp.telegramwebhookapi.payload.DTO.AerodromeDTO;
import uz.pdp.telegramwebhookapi.payload.InlineDTO;
import uz.pdp.telegramwebhookapi.service.inter.AerodromeService;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;
import uz.pdp.telegramwebhookapi.util.Utils;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AerodromeServiceImpl implements AerodromeService {
    private final AviaSalesFeign aviaSalesFeign;
    private final BaseService baseService;
    private final TelegramFeign telegramFeign;
    private static List<Country> countryList = new ArrayList<>();

    HashMap<Long, AerodromeDTO> map = new HashMap<>();
    HashMap<Long, UUID> deleteMap = new HashMap<>();

    @Override
    public void addAerodrome(Message message, String data, String inlineMessageId,boolean isEdite) {
        Long chatId = message.getChatId();
        String text = message.getText();
        AerodromeDTO aerodromeDTO = map.get(chatId);
        if (aerodromeDTO == null) {

            map.put(chatId, new AerodromeDTO());
            baseService.getMenu(chatId, "enter aerodrome name", Utils.EXIT);

        } else if (aerodromeDTO.getName() == null) {
            if (Objects.equals(text, Buttons.EXIT) || Objects.equals(text, "choose country")) {
                clearMap(chatId);
                clearDeleteAndEditeMap(chatId);
                return;
            }

            aerodromeDTO.setName(text);
            map.put(chatId, aerodromeDTO);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("choose country");
            ReplyKeyboard replyKeyboard = getInlineMarkupCountry(chatId);
            if (replyKeyboard == null) return;
            sendMessage.setReplyMarkup(replyKeyboard);

            baseService.send(sendMessage);
        } else if (aerodromeDTO.getCountryId() == null) {
            if (Objects.equals(data, "x")) {
                clearMap(chatId);
                clearDeleteAndEditeMap(chatId);
                baseService.deleteMarkup(message.getChatId(), message.getMessageId(), inlineMessageId);
            } else if (data != null) {
                aerodromeDTO.setCountryId(Integer.valueOf(data));
                map.put(chatId, aerodromeDTO);
                baseService.getMenu(chatId,
                        "\uD83D\uDCDD<strong>name</strong> : " + aerodromeDTO.getName() +
                                "\n\uD83C\uDFF3️<strong>country</strong> : " + getNameAerodrome(Integer.valueOf(data)),
                        Utils.COMFIRM);
            } else baseService.send(new SendMessage(chatId.toString(), "ketmon bittasini tanla"));
        } else {
            map.put(chatId, null);
            if (text.equals(Buttons.OK)) {
                addAero(aerodromeDTO, chatId,isEdite);
            } else {
                baseService.send(new SendMessage(chatId.toString(), "success canceled"));
                getAerodromeMenu(chatId);
            }
            deleteMap.put(chatId,null);
        }

    }



    @Override
    public void editAerodrome(Message message, String data, String inlineMessageId) {
        Long chatId = message.getChatId();
        String text = message.getText();

        UUID id = deleteMap.get(chatId);
        if (id != null) {
            if (Objects.equals(text, Buttons.NAME)) {
                baseService.exitState(chatId, State.EXTRA_AERO_NAME_STATE);
                editName(message, chatId, text);
            } else if (Objects.equals(text, "All")) {
                baseService.exitState(chatId, State.EXTRA_AERO_EDIT_ALL);
                addAerodrome(message, data, inlineMessageId,true);
            } else if (Objects.equals(text, Buttons.EXIT)) {
                deleteMap.put(chatId, null);
                baseService.send(new SendMessage(chatId.toString(), "success canceled" + Buttons.X));
                editAerodrome(message, data, inlineMessageId);
            } else baseService.getMenu(chatId, "choose param", Utils.AERODROM_EDIT);
        } else show(null, message, data, inlineMessageId, true);
    }

    @Override
    public void editName(Message message, Long chatId, String text) {
        if (Objects.equals(Buttons.NAME, text)) {
            baseService.getMenu(chatId, "enter name", Utils.EXIT);
        } else if (Objects.equals(Buttons.EXIT, text)) {
            exit(message);
        } else {
            try {
                aviaSalesFeign.editAerodrome(
                        baseService.getAuthentication(),
                        deleteMap.get(chatId),
                        new AerodromeDTO(text, null)
                );
                baseService.send(new SendMessage(chatId.toString(), "success edited" + Buttons.OK));
            } catch (Exception exception) {
                baseService.send(new SendMessage(chatId.toString(), "Some wrong"));
            } finally {
                baseService.exitState(chatId, State.ADM_AERODROM_EDITE_STATE);
                deleteMap.put(chatId, null);
                show(true, message, null, null, false);
            }
        }
    }

    @Override
    public void deleteAerodrome(Message message, String data, String inlineMessageId) {
        Long chatId = message.getChatId();
        String text = message.getText();

        UUID id = deleteMap.get(chatId);
        if (id != null) {

            if (Objects.equals(text, Buttons.OK)) {
                try {
                    aviaSalesFeign.deleteAerodromeById(baseService.getAuthentication(), id);
                    baseService.send(new SendMessage(chatId.toString(), "success deleted" + Buttons.OK));
                } catch (Exception e) {
                    baseService.send(new SendMessage(chatId.toString(), "some wrong"));
                } finally {
                    deleteMap.put(chatId, null);
                    show(true, message, data, inlineMessageId, false);
                }
            } else if (Objects.equals(text, Buttons.X)) {
                deleteMap.put(chatId, null);
                baseService.send(new SendMessage(chatId.toString(), "success canceled" + Buttons.X));
                show(true, message, data, inlineMessageId, false);
            } else baseService.getMenu(chatId, "are you sure", Utils.COMFIRM);
        } else show(true, message, data, inlineMessageId, true);
    }

    @Override
    public void showAerodrome(Message message, String data, String inlineMessageId) {
        show(false, message, data, inlineMessageId, true);
    }

    @Override
    public void showAerodromeById(Long chatId, String text) {

    }

    @Override
    public void getAerodromeMenu(Long chatId) {
        baseService.getMenu(chatId, "aerodrome menu", Utils.ADMIN_AERODROM_MENU);
        baseService.exitState(chatId, State.ADM_AERDROM_STATE);
    }


    private void addAero(AerodromeDTO aerodromeDTO, Long chatId, boolean isEdite) {
        try {
            if (!isEdite) {
                aviaSalesFeign.addAerodrome(
                        aerodromeDTO,
                        baseService.getAuthentication()
                );
                baseService.send(new SendMessage(chatId.toString(), "success added"));
            }else {
                System.out.println("deleteMap aerodrome = " + deleteMap.get(chatId));
                aviaSalesFeign.editAerodrome(
                        baseService.getAuthentication(),
                        deleteMap.get(chatId),
                        aerodromeDTO
                        );
                baseService.send(new SendMessage(chatId.toString(), "success edited"));
            }
        } catch (Exception e) {
            baseService.send(new SendMessage(chatId.toString(), "some wrong"));
        } finally {
            clearMap(chatId);
            clearDeleteAndEditeMap(chatId);
        }

    }



    private String getNameAerodrome(Integer data) {
        return countryList.stream()
                .filter(obj -> Objects.equals(data, obj.getId()))
                .map(Country::getName)
                .findFirst()
                .orElse(null);
    }

    private ReplyKeyboard getInlineMarkupCountry(Long chatId) {
        try {
            ResponseEntity<List<Country>> response = aviaSalesFeign
                    .getCountry(baseService.getAuthentication());
            countryList = response.getBody();
            assert countryList != null;
            System.out.println(countryList);
            return GetReplyMarkUp.getInlineKeyboard(baseService.getArrayCountry(countryList, "", "", true));
        } catch (Exception exception) {
            baseService.send(new SendMessage(chatId.toString(), "some wrong"));
            clearMap(chatId);
            clearDeleteAndEditeMap(chatId);
            return null;
        }
    }

    private void clearMap(Long chatId) {
        map.put(chatId, null);
        getAerodromeMenu(chatId);
    }
    private void clearDeleteAndEditeMap(Long chatId) {
        deleteMap.put(chatId,null);
    }

    private void writeAerodromeDetail(Long chatId, Aerodrome aer) {
        //name lani qoraytib yozish kere
        String detail = "🛩️ Aerodrome info\n\uD83D\uDCE9 name : " + aer.getName() + "\n\uD83C\uDFF3️ country : " + aer.getCountry().getName();
        baseService.send(new SendMessage(chatId.toString(), detail));
    }

    private void show(Boolean isDelete, Message message, String data, String inlineMessageId, boolean blabla) {
        Long chatId = message.getChatId();
        String text = message.getText();
        if (Objects.equals(text, Buttons.EXIT)) {
            getAerodromeMenu(chatId);
            return;
        }
        List<InlineDTO> aerodromeList;
        try {
            ResponseEntity<List<InlineDTO>> response = aviaSalesFeign.getAllAerodrome(baseService.getAuthentication());
            aerodromeList = response.getBody();
        } catch (Exception exception) {
            baseService.send(new SendMessage(chatId.toString(), "some wrong"));
            getAerodromeMenu(chatId);
            return;
        }
        if (!Objects.equals(text, Buttons.AERS_SHOW) && !Objects.equals(text, Buttons.AER_DELETE)
                && text != null && !Objects.equals(text, Buttons.AER_EDIT) && blabla) {
            assert aerodromeList != null;
            baseService.getMenu(chatId, "✈", baseService.getArray(aerodromeList, "", "", false));
            for (InlineDTO aerodrome : aerodromeList) {
                if (Objects.equals(aerodrome.getName(), text)) {
                    try {
                        Aerodrome aer = aviaSalesFeign.getAerodromeById(baseService.getAuthentication(), aerodrome.getId()).getBody();
                        Objects.requireNonNull(aer);
                        writeAerodromeDetail(chatId, aer);
                        if (isDelete == null) {
                            deleteMap.put(chatId, aer.getId());
                            editAerodrome(message, null, null);
                        } else if (isDelete) {
                            deleteMap.put(chatId, aer.getId());
                            deleteAerodrome(message, null, null);
                        }
                    } catch (Exception e) {
                        baseService.send(new SendMessage(chatId.toString(), "some wrong"));
                    }
                    return;
                }
            }
            baseService.send(new SendMessage(chatId.toString(), "ketmon bittasinbi tanla"));
        } else {
            assert aerodromeList != null;
            baseService.getMenu(chatId, "choose aerodrome", baseService.getArray(aerodromeList, "", "", false));
        }
    }

    private void exit(Message message) {
        baseService.exitState(message.getChatId(), State.ADM_AERODROM_EDITE_STATE);
        editAerodrome(message, null, null);
    }

}
