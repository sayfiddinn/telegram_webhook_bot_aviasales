package uz.pdp.telegramwebhookapi.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.telegramwebhookapi.Entity.ChatUser;


public interface UserRepositary extends JpaRepository<ChatUser, Long> {
}
