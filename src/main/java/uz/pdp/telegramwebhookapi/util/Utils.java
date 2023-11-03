package uz.pdp.telegramwebhookapi.util;

import uz.pdp.telegramwebhookapi.Bt.Buttons;
import uz.pdp.telegramwebhookapi.payload.Country;

import java.util.ArrayList;
import java.util.List;

public interface Utils {
    String username="http://t.me/webhukmazgi_bot";
    String token="bot6159804786:AAHDtTrexOPOqclr7rhPz8SkEwiB6eC7Jx8";

    String TELEGRAM_BASE_URL_WITHOUT_BOT="https://api.telegram.org/";
    String AVIA_BASE_URL="https://ecdf-213-230-118-101.ngrok.io/";
    String BASIC = "Basic ";
    String[][] ADMIN_MAIN_MENU ={
            {Buttons.MODERATOR,Buttons.PLANE},
            {Buttons.AERODROM,Buttons.SOLDTICKETS}
    };
    String[][] ADMIN_PLANE_MENU ={
            {Buttons.PLANE_ADD,Buttons.PLANE_DELETE},
            {Buttons.PLANE_EDIT,Buttons.PLANES_SHOW},
            {Buttons.EXIT}
    };
//    782651398
    List<Long> adminList=List.of(856845004L);
    String[][] ADMIN_AERODROM_MENU ={
            {Buttons.AER_ADD,Buttons.AER_EDIT},
            {Buttons.AER_SHOW,Buttons.AERS_SHOW},
            {Buttons.AER_DELETE,Buttons.EXIT}
    };
    String[][] COUNTRY ={
            {Buttons.UZBEKISTAN},
            {Buttons.AMERICA},
            {Buttons.RUSSIA}
    };
    String[][] EXIT = {
            {Buttons.EXIT}
    };
    String[][] COMFIRM = {
            {Buttons.OK,Buttons.X}
    };
    String[][] AERODROM_EDIT = {
            {Buttons.NAME,"All"},
            {Buttons.EXIT}
    };
    String[][] PLANE_EDIT = {
            {Buttons.NAME,Buttons.COMPANY_NAME},
            {Buttons.PLANE_NUMBER,Buttons.SEATS_NUMBER},
            {Buttons.PLANE_AERODROME,"All"},
            {Buttons.EXIT}
    };
    static List<Country>countryList=new ArrayList<>();

}
//https://api.telegram.org/bot6132101113:AAFzEIO-rTpkXXzMI3xBK5HUixEN5g9ORpM/sendMessage