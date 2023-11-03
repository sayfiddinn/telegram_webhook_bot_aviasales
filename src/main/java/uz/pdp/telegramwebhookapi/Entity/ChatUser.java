package uz.pdp.telegramwebhookapi.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.telegramwebhookapi.Entity.enums.State;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatUser {
    @Id
    private Long chatId;
    @Enumerated(EnumType.STRING)
    private State state;

}
