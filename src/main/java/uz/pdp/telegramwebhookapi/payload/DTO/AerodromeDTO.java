package uz.pdp.telegramwebhookapi.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AerodromeDTO {
    private String name;
    private Integer countryId;
}
