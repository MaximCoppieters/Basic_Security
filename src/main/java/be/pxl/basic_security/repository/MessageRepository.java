package be.pxl.basic_security.repository;

import be.pxl.basic_security.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository
        extends JpaRepository<Message, Long> {
}
