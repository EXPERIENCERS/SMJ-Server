package com.experiencers.server.smj.repository;

import com.experiencers.server.smj.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySender(String Sender);
    List<Message> findAllByReceiver(String Receiver);
}
