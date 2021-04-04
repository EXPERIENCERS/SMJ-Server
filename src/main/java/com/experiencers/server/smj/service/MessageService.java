package com.experiencers.server.smj.service;

import com.experiencers.server.smj.domain.Member;
import com.experiencers.server.smj.domain.Message;
import com.experiencers.server.smj.dto.MessageDto;
import com.experiencers.server.smj.manager.MemberManager;
import com.experiencers.server.smj.repository.MemberRepository;
import com.experiencers.server.smj.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberManager memberManager;


    ////////////////////////////////////////////////////
    //API Service
    ////////////////////////////////////////////////////

    //Api Service
    public MessageDto.MessageDtoResponse saveMessage(MessageDto.MessageDtoRequest messageDto) {
        Member sendMember = memberManager.getMember();

        Message message = messageDto.toEntity(sendMember.getEmail());
        Message saveMessage = messageRepository.save(message);

        return MessageDto.MessageDtoResponse.of(saveMessage);
    }

    public List<MessageDto.MessageDtoResponse> readAllMessage() {
        String email = memberManager.getEmailOfMember();
        List<Message> messages = messageRepository.findAllByReceiver(email);

        return MessageDto.MessageDtoResponse.of(messages);
    }

    public void deleteMessage(Long message_id){
        messageRepository.deleteById(message_id);
    }

    ////////////////////////////////////////////////////
    //Admin Service
    ////////////////////////////////////////////////////
    public Message writeMessage(Message inputtedMessage) {
        Message savedMessage = messageRepository.save(inputtedMessage);

        return savedMessage;
    }

    public Message readMessage(Long message_id) {
        return messageRepository.findById(message_id).get();
    }

    public List<Message> readMessage() {
        return messageRepository.findAll();
    }


    public void removeMessage(Long messageId){
        messageRepository.deleteById(messageId);
    }


    public void updateMessage(Long messageId, Message message){
        Message beforeMessage = messageRepository.findById(messageId).get();

        beforeMessage.setContent(message.getContent());
        beforeMessage.setSender(message.getSender());
        beforeMessage.setReceiver(message.getReceiver());
        beforeMessage.setCreateAt(message.getCreateAt());
        messageRepository.save(beforeMessage);
    }


}

