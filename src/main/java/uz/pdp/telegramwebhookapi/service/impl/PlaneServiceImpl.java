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
import uz.pdp.telegramwebhookapi.payload.DTO.PlaneDTO;
import uz.pdp.telegramwebhookapi.payload.InlineDTO;
import uz.pdp.telegramwebhookapi.payload.Plane;
import uz.pdp.telegramwebhookapi.service.inter.BaseService;
import uz.pdp.telegramwebhookapi.service.inter.PlaneService;
import uz.pdp.telegramwebhookapi.util.Utils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PlaneServiceImpl implements PlaneService {
    private final AviaSalesFeign aviaSalesFeign;
    private final BaseService baseService;
    HashMap<Long, PlaneDTO> map = new HashMap<>();
    HashMap<Long, UUID> deleteMap = new HashMap<>();
    private static List<InlineDTO> aerodromeList = new ArrayList<>();

    @Override
    public void addPlane(Message message, String data, String inlineMessageId,boolean isEdite) {
        Long chatId = message.getChatId();
        String text = message.getText();
        PlaneDTO planeDTO = map.get(chatId);
        if (planeDTO == null) {

            map.put(chatId, new PlaneDTO());
            baseService.getMenu(chatId, "enter plane name", Utils.EXIT);

        } else {
            boolean isExit = Objects.equals(text, Buttons.EXIT) || Objects.equals(text, "choose aerodrome");
            if (planeDTO.getName() == null) {
                if (isExit) {
                    clearMap(chatId);
                    clearDeleteAndEditeMap(chatId);
                    return;
                }

                planeDTO.setName(text);
                map.put(chatId, planeDTO);
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText("choose aerodrome");
                ReplyKeyboard replyKeyboard = getInlineMarkupAerodrome(chatId);
                if (replyKeyboard == null) return;
                sendMessage.setReplyMarkup(replyKeyboard);

                baseService.send(sendMessage);
            } else if (planeDTO.getAerodromeId() == null) {
                if (Objects.equals(data, "x")) {
                    clearMap(chatId);
                    clearDeleteAndEditeMap(chatId);
                    baseService.deleteMarkup(message.getChatId(), message.getMessageId(), inlineMessageId);
                } else if (data != null) {
                    planeDTO.setAerodromeId(UUID.fromString(data));
                    map.put(chatId, planeDTO);
                    baseService.getMenu(chatId, "enter plane company name", Utils.EXIT);
                } else baseService.send(new SendMessage(chatId.toString(), "ketmon bittasini tanla"));
            } else if (planeDTO.getPlaneCompany() == null) {
                if (isExit) {
                    clearMap(chatId);
                    clearDeleteAndEditeMap(chatId);
                    return;
                }
                planeDTO.setPlaneCompany(text);
                map.put(chatId, planeDTO);
                baseService.getMenu(chatId, "enter plane number", Utils.EXIT);


            } else if (planeDTO.getPlaneNumber() == null) {
                if (isExit) {
                    clearMap(chatId);
                    clearDeleteAndEditeMap(chatId);
                    return;
                }
                planeDTO.setPlaneNumber(text);
                map.put(chatId, planeDTO);
                baseService.getMenu(chatId, "enter plane seats number", Utils.EXIT);

            } else if (planeDTO.getSeats() == null) {
                if (isExit) {
                    clearMap(chatId);
                    clearDeleteAndEditeMap(chatId);
                    return;
                }
                planeDTO.setSeats(Integer.valueOf(text));
                map.put(chatId, planeDTO);
                String word="\uD83D\uDCDD<strong>name</strong> : " + planeDTO.getName() +
                        "\n\uD83D\uDEE9<strong>aerodrome </strong> : " + getNameAerodrome(planeDTO.getAerodromeId())+
                        "\n\uD83D\uDCDD<strong>company name </strong> : "+planeDTO.getPlaneCompany()+
                        "\n\uD83D\uDCCB<strong>plane number </strong> : "+planeDTO.getPlaneNumber()+
                        "\n\uD83D\uDCCB<strong>plane seats number </strong> : "+planeDTO.getSeats();
                baseService.getMenu(chatId,word,Utils.COMFIRM);
            }
            else {
                map.put(chatId, null);
                if (text.equals(Buttons.OK)) {
                    addPlane(planeDTO, chatId,isEdite);
                } else {
                    baseService.send(new SendMessage(chatId.toString(), "success canceled"));
                    getPlaneMenu(chatId);
                }
                deleteMap.put(chatId,null);
            }
        }
    }

    @Override
    public void deletePlane(Message message, String data, String inlineMessageId) {
        Long chatId = message.getChatId();
        String text = message.getText();

        UUID id = deleteMap.get(chatId);
        if (id != null) {

            if (Objects.equals(text, Buttons.OK)) {
                try {
                    System.out.println("id = " + id);
                    aviaSalesFeign.deletePlaneById(baseService.getAuthentication(), id);
                    baseService.send(new SendMessage(chatId.toString(), "success deleted" + Buttons.OK));
                } catch (Exception e) {
                    System.out.println("e = " + e);
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
    public void editePlane(Message message, String data, String inlineMessageId) {
        Long chatId = message.getChatId();
        String text = message.getText();

        UUID id = deleteMap.get(chatId);
        if (id != null) {
            if (Objects.equals(text, Buttons.NAME)) {
                baseService.exitState(chatId, State.EXTRA_PLANE_EDIT_NAME);
                editPlaneElement(message, chatId, text,State.EXTRA_PLANE_EDIT_NAME);
            } else if (Objects.equals(text, Buttons.COMPANY_NAME)){
                baseService.exitState(chatId, State.EXTRA_PLANE_EDIT_COMPANY);
                editPlaneElement(message, chatId, text,State.EXTRA_PLANE_EDIT_COMPANY);
            }else if (Objects.equals(text, Buttons.PLANE_NUMBER)){
                baseService.exitState(chatId, State.EXTRA_PLANE_EDIT_NUMBER);
                editPlaneElement(message, chatId, text,State.EXTRA_PLANE_EDIT_NUMBER);
            }else if (Objects.equals(text, Buttons.SEATS_NUMBER)){
                baseService.exitState(chatId, State.EXTRA_PLANE_EDIT_SEATS);
                editPlaneElement(message, chatId, text,State.EXTRA_PLANE_EDIT_SEATS);
            }
            else if (Objects.equals(text, "All")) {
                baseService.exitState(chatId, State.EXTRA_PLANE_EDIT_ALL);
                addPlane(message, data, inlineMessageId,true);
            } else if (Objects.equals(text, Buttons.EXIT)) {
                deleteMap.put(chatId, null);
                baseService.send(new SendMessage(chatId.toString(), "success canceled" + Buttons.X));
                editePlane(message, data, inlineMessageId);
            } else baseService.getMenu(chatId, "choose param", Utils.PLANE_EDIT);
        } else show(null, message, data, inlineMessageId, true);
    }
    @Override
    public void editPlaneElement(Message message, Long chatId, String text, State state) {
        if (Objects.equals(Buttons.NAME, text) ||Objects.equals(Buttons.COMPANY_NAME, text) ||
                Objects.equals(Buttons.PLANE_NUMBER, text) ||Objects.equals(Buttons.SEATS_NUMBER, text) ||
                Objects.equals(Buttons.PLANE_AERODROME, text)) {
            baseService.getMenu(chatId, "enter"+text.split(" ")[1], Utils.EXIT);
        } else if (Objects.equals(Buttons.EXIT, text)) {
            exitPlane(message);
        } else {
            try {
                PlaneDTO planeDTO=new PlaneDTO();
                if (Objects.equals(state,State.EXTRA_PLANE_EDIT_NAME))
                    planeDTO.setName(text);
                if (Objects.equals(state,State.EXTRA_PLANE_EDIT_COMPANY))
                    planeDTO.setPlaneCompany(text);
                if (Objects.equals(state,State.EXTRA_PLANE_EDIT_NUMBER))
                    planeDTO.setPlaneNumber(text);
                if (Objects.equals(state,State.EXTRA_PLANE_EDIT_SEATS))
                    planeDTO.setSeats(Integer.valueOf(text));
                System.out.println("edit planeDTO = " + planeDTO);
                System.out.println("deleteMap.get(chatId) = " + deleteMap.get(chatId));
                aviaSalesFeign.editePlane(
                        baseService.getAuthentication(),
                        deleteMap.get(chatId),
                        planeDTO
                );
                baseService.send(new SendMessage(chatId.toString(), "success edited" + Buttons.OK));
            } catch (Exception exception) {
                System.out.println("exception = " + exception);
                baseService.send(new SendMessage(chatId.toString(), "Some wrong"));
            } finally {
                baseService.exitState(chatId, State.ADM_PLANE_EDITE_STATE);
                deleteMap.put(chatId, null);
                show(true, message, null, null, false);
            }
        }
    }

    private void exitPlane(Message message) {
        baseService.exitState(message.getChatId(), State.ADM_PLANE_EDITE_STATE);
        editePlane(message, null, null);
    }


    @Override
    public void showPlane(Message message,String data, String inlineMessageId) {
        show(false, message, data, inlineMessageId, true);
    }


    @Override
    public void getPlaneMenu(Long chatId) {
        baseService.getMenu(chatId, "plane menu", Utils.ADMIN_PLANE_MENU);
        baseService.exitState(chatId, State.ADM_PLANE_STATE);
    }

    private void clearMap(Long chatId) {
        map.put(chatId, null);
        getPlaneMenu(chatId);
    }
    private void clearDeleteAndEditeMap(Long chatId) {
        deleteMap.put(chatId,null);
    }
    private ReplyKeyboard getInlineMarkupAerodrome(Long chatId) {
        try {
            ResponseEntity<List<InlineDTO>> response = aviaSalesFeign
                    .getAllAerodrome(baseService.getAuthentication());
            aerodromeList = response.getBody();
            assert aerodromeList != null;
            return GetReplyMarkUp.getInlineKeyboard(baseService.getArray(aerodromeList, "", "", true));
        } catch (Exception exception) {
            baseService.send(new SendMessage(chatId.toString(), "some wrong"));
            clearMap(chatId);
            clearDeleteAndEditeMap(chatId);
            return null;
        }
    }
    private void addPlane(PlaneDTO planeDTO, Long chatId, boolean isEdite) {
        try {
            if (!isEdite) {
                aviaSalesFeign.addPlane(
                        planeDTO,
                        baseService.getAuthentication()
                );
                baseService.send(new SendMessage(chatId.toString(), "success added"));
            }else {

                aviaSalesFeign.editePlane(
                        baseService.getAuthentication(),
                        deleteMap.get(chatId),
                        planeDTO
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

    private String getNameAerodrome(UUID data) {
        System.out.println("data = " + data);
        return aerodromeList.stream()
                .filter(obj -> Objects.equals(data, obj.getId()))
                .map(InlineDTO::getName)
                .findFirst()
                .orElse(null);
    }

    private void show(Boolean isDelete, Message message, String data, String inlineMessageId, boolean blabla) {
        Long chatId = message.getChatId();
        String text = message.getText();
        if (Objects.equals(text, Buttons.EXIT)) {
            getPlaneMenu(chatId);
            return;
        }
        List<InlineDTO> planeList;
        try {
            ResponseEntity<List<InlineDTO>> response = aviaSalesFeign.getAllPlane(baseService.getAuthentication());
            planeList = response.getBody();
        } catch (Exception exception) {
            baseService.send(new SendMessage(chatId.toString(), "some wrong"));
            getPlaneMenu(chatId);
            return;
        }
        assert planeList != null;
        if (!Objects.equals(text, Buttons.PLANES_SHOW) && !Objects.equals(text, Buttons.PLANE_DELETE)
                && text != null && !Objects.equals(text, Buttons.PLANE_EDIT) && blabla) {
            baseService.getMenu(chatId, "\uD83D\uDEE9", baseService.getArray(planeList, "", "", false));
            for (InlineDTO plane : planeList) {
                if (Objects.equals(plane.getName(), text)) {
                    try {
                        Plane plan = aviaSalesFeign.getPlaneById(baseService.getAuthentication(), plane.getId()).getBody();
                        Objects.requireNonNull(plan);
                        writePlaneDetail(chatId, plan);
                        if (isDelete == null) {
                            deleteMap.put(chatId, plan.getId());
                            editePlane(message, null, null);
                        } else if (isDelete) {
                            deleteMap.put(chatId, plan.getId());
                            deletePlane(message, null, null);
                        }
                    } catch (Exception e) {
                        baseService.send(new SendMessage(chatId.toString(), "some wrong"));
                    }
                    return;
                }
            }
            baseService.send(new SendMessage(chatId.toString(), "ketmon bittasinbi tanla"));
        } else {
            baseService.getMenu(chatId, "choose aerodrome", baseService.getArray(planeList, "", "", false));
        }
    }
    private void writePlaneDetail(Long chatId, Plane plane) {
        //name lani qoraytib yozish kere
        String detail = "🛩️ Plane info\n\uD83D\uDCE9 name : " + plane.getName()
                + "\n\uD83D\uDCE9 plane company : " + plane.getPlaneCompany()
                + "\n\uD83D\uDCCB plane number : "+plane.getPlaneNumber()
                + "\n\uD83D\uDCCB plane seats number : "+plane.getSeats();
        baseService.send(new SendMessage(chatId.toString(), detail));
    }



}
