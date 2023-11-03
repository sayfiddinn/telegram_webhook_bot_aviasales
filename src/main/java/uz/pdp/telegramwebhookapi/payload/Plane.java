package uz.pdp.telegramwebhookapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plane {
    private UUID id;
    private String name;
    private Integer seats;
    private String planeNumber;
    private String planeCompany;
}
