package com.experiencers.server.smj.service;


import com.experiencers.server.smj.domain.Alarm;
import com.experiencers.server.smj.domain.Member;
import com.experiencers.server.smj.dto.AlarmDto;
import com.experiencers.server.smj.enumerate.RepeatType;
import com.experiencers.server.smj.manager.MemberManager;
import com.experiencers.server.smj.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private MemberManager memberManager;

    //Api Service
    public AlarmDto.AlarmDtoResponse saveAlarm(AlarmDto.AlarmDtoRequest alarmDto){
        boolean check = false;
        Member member = memberManager.getMember();

        RepeatType repeatType[] = RepeatType.values();

        for(RepeatType rt : repeatType){
            if(rt.toString().equals(alarmDto.getRepeat().toString())){
                check = true;
            }
        }
        if(check == false){
            alarmDto.setRepeat(RepeatType.ONCE);

        }

        Alarm inputtedAlarm = alarmDto.toEntity(member);

        Alarm savedAlarm = alarmRepository.save(inputtedAlarm);
        return AlarmDto.AlarmDtoResponse.of(savedAlarm);
    }
    public AlarmDto.AlarmDtoResponse readAndUpdateAlarm(Long alarmId, AlarmDto.AlarmDtoRequest alarmDto) {
        Optional<Alarm> data = alarmRepository.findById(alarmId);

        if (!data.isPresent()) {
            return null;
        }

        Alarm updateData = data.get();
        updateData.setTitle(alarmDto.getTitle());
        updateData.setContent(alarmDto.getContent());
        updateData.setStartDate(alarmDto.getStartDate());
        updateData.setStartTime(alarmDto.getStartTime());
        updateData.setEndTime(alarmDto.getEndTime());
        updateData.setRepeat(alarmDto.getRepeat());

        Alarm updatedalarm = alarmRepository.save(updateData);

        return AlarmDto.AlarmDtoResponse.of(updatedalarm);
    }
    public List<AlarmDto.AlarmDtoResponse> readAllAlarm(){
        Member member = memberManager.getMember();

        List<Alarm> alarms = member.getAlarms();


        return AlarmDto.AlarmDtoResponse.of(alarms);
    }

    public List<AlarmDto.AlarmDtoResponse> readAllAlarmOfDate(String startDate){
        Member member = memberManager.getMember();

        List<Alarm> alarmList = alarmRepository.findAllByMemberEqualsAndStartDate(member,startDate);

        return AlarmDto.AlarmDtoResponse.of(alarmList);
    }

    public AlarmDto.AlarmDtoResponse readAllAlarmOfId(Long id){
        Alarm alarm = alarmRepository.findById(id).orElse(null);

        return AlarmDto.AlarmDtoResponse.of(alarm);
    }

    public void removeAlarm(Long alarmId){
        alarmRepository.deleteById(alarmId);
    }
    //Admin Service
    public Alarm saveAlarmOfMember(Alarm inputtedAlarm){
        Alarm savedAlarm = alarmRepository.save(inputtedAlarm);

        return savedAlarm;
    }


    public Alarm readAlarm(Long alarmId){
        return alarmRepository.findById(alarmId).get();
    }

    public List<Alarm> readAllAlarmOfMember(){
        return alarmRepository.findAll();
    }

    public Alarm readAndUpdateAlarmOfMember(Long alarmId, Alarm alarm) {
        Optional<Alarm> data = alarmRepository.findById(alarmId);

        if (!data.isPresent()) {
            return null;
        }

        Alarm updateData = data.get();
        updateData.setTitle(alarm.getTitle());
        updateData.setContent(alarm.getContent());
        updateData.setStartDate(alarm.getStartDate());
        updateData.setStartTime(alarm.getStartTime());
        updateData.setEndTime(alarm.getEndTime());
        updateData.setRepeat(alarm.getRepeat());

        Alarm updatedalarm = alarmRepository.save(updateData);

        return updatedalarm;
    }
}
