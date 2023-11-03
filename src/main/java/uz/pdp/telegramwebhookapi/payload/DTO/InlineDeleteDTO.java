package uz.pdp.telegramwebhookapi.payload.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InlineDeleteDTO {
    Integer messageId;
    String inlineMessageId;
}
