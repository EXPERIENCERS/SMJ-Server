package com.experiencers.server.smj.controller;

import com.experiencers.server.smj.domain.Message;
import com.experiencers.server.smj.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin/message")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("")
    public ModelAndView getIndex() {
        List<Message> messageList = messageService.readMessage();

        ModelAndView response = new ModelAndView("message/info");//뷰이름설정
        response.addObject(messageList);//뷰로 보낼 데이터 userlist

        return response;

    }
    @PostMapping("")
    public String postMessage(@ModelAttribute Message inputtedMessage) {
        Message savedMessage = messageService.writeMessage(inputtedMessage);

        return "redirect:/admin/message";
    }

    /*@GetMapping("/{id}")
>>>>>>> main
    public ModelAndView getPost(@PathVariable("id") Long message_id) {
        Message message = messageService.readMessage(message_id);

        ModelAndView response = new ModelAndView("message/detail");
        response.addObject(message);

        return response;
    }*/

    @PostMapping("/{message_id}/delete")
    public String deleteMessage(@PathVariable("message_id") Long messageId, HttpServletRequest request){
        messageService.removeMessage(messageId);
        return "redirect:"+request.getHeader("referer");
    }

    @GetMapping("/{message_id}/edit")
    public ModelAndView editMessage(@PathVariable("message_id")Long message_id){
        Message message = messageService.readMessage(message_id);
        ModelAndView response = new ModelAndView("message/edit");
        response.addObject(message);
        return response;
    }


    @PostMapping("/{message_id}/update")
    public String updateMessage(@PathVariable("message_id") Long messageId, Message message){
        messageService.updateMessage(messageId, message);
        return "redirect:/admin/message";

    }
}
