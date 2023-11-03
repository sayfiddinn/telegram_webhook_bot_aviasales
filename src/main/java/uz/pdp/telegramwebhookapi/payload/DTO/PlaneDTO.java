package uz.pdp.telegramwebhookapi.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaneDTO {
    private String name;
    private Integer seats;
    private String planeNumber;
    private String planeCompany;
    private UUID aerodromeId;
}
