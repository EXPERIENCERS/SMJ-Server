package com.experiencers.server.smj.controller;


import com.experiencers.server.smj.domain.Alarm;
import com.experiencers.server.smj.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/admin/alarm")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @GetMapping("")
    public ModelAndView getIndex(){
        //List<Alarm> alarmList = alarmService.readAllAlarm();

        ModelAndView response = new ModelAndView("alarm/index");
        //response.addObject(alarmList);

        return response;
    }

    @PostMapping("")
    public String createAlarm(@ModelAttribute Alarm inputtedAlarm){
        System.out.println(inputtedAlarm.toString());
        //Alarm savedAlarm = alarmService.saveAlarm(inputtedAlarm);

        return "redirect:/alarm";
    }

    @PostMapping("/{alarm_id}/delete")
    public String deleteAlarm(@PathVariable("alarm_id") Long alarm_id){
        alarmService.removeAlarm(alarm_id);

        return "redirect:/alarm";
    }

    @PostMapping("/{alarm_id}/edit")
    public ModelAndView editAlarm(@PathVariable("alarm_id") Long alarm_id){
        Alarm alarm = alarmService.readAlarm(alarm_id);

        ModelAndView response = new ModelAndView("alarm/edit");
        response.addObject(alarm);

        return response;
    }

    @PostMapping("/{alarm_id}/edit/update")
    public String updateAlarm(Alarm alarm){
        alarmService.updateAlarm(alarm);

        return "redirect:/alarm";
    }



}
