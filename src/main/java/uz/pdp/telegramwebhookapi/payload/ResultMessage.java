package uz.pdp.telegramwebhookapi.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessage<T> {
    private Boolean success;

    private T object;
}
