package uz.pdp.telegramwebhookapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aerodrome {
    private UUID id;
    private String name;
    private Country country;

}

