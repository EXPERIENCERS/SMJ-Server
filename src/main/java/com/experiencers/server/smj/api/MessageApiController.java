package com.experiencers.server.smj.api;


import com.experiencers.server.smj.domain.Board;
import com.experiencers.server.smj.domain.Message;
import com.experiencers.server.smj.dto.MessageDto;
import com.experiencers.server.smj.service.MessageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Messages", description = "쪽지")
@RestController
@RequestMapping("/api/messages")
public class MessageApiController {

    @Autowired
    private MessageService messageService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiOperation(value = "쪽지 목록",notes = "성공시 사용자가 받은 모든 쪽지를 반환합니다.",response = MessageDto.MessageDtoResponse.class)
    @GetMapping("/received")
    public ResponseEntity<?> receiveMessages(){
        List<MessageDto.MessageDtoResponse> messageList = messageService.receivedAllMessage();

        return new ResponseEntity<>(messageList,HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiOperation(value = "쪽지 목록",notes = "성공시 사용자가 보낸 모든 쪽지를 반환합니다.",response = MessageDto.MessageDtoResponse.class)
    @GetMapping("/sent")
    public ResponseEntity<?> sendMessages(){
        List<MessageDto.MessageDtoResponse> messageList = messageService.sentAllMessage();

        return new ResponseEntity<>(messageList,HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 201, message = "작성됨")
    })
    @ApiOperation(value = "쪽지 작성",notes = "성공시 받은 사람에게 쪽지가 저장합니다.")
    @PostMapping("")
    public ResponseEntity<?> postMessages(@RequestBody MessageDto.MessageDtoRequest messageDto){
        MessageDto.MessageDtoResponse savedMessage = messageService.saveMessage(messageDto);


        return new ResponseEntity<>(savedMessage,HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "콘텐츠 없음")
    })
    @ApiImplicitParam(name = "message_id",value = "쪽지번호",required = true,paramType = "path")
    @ApiOperation(value = "쪽지 삭제",notes = "성공시 해당 쪽지를 삭제합니다.")
    @DeleteMapping("/{message_id}")
    public ResponseEntity<?> deleteMessages(@PathVariable("message_id")Long messageId){
        messageService.deleteMessage(messageId);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        data.put("message_id", messageId);
        result.put("message", data);

        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }
}
