package com.experiencers.server.smj.api;


import com.experiencers.server.smj.dto.AlarmDto;
import com.experiencers.server.smj.service.AlarmService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "Alarms", description = "알람")
@RestController
@RequestMapping("/api/alarms")
public class AlarmApiController {

    @Autowired
    private AlarmService alarmService;

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiImplicitParam(name = "startDate",value = "해당날짜",required = true,paramType = "path",example = "yyyy-MM-dd")
    @ApiOperation(value = "해당날짜 알람 목록",notes = "성공시 사용자의 해당하는 날짜의 모든 알람을 반환합니다.",response = AlarmDto.AlarmDtoResponse.class)
    @GetMapping("/startDate/{startDate}")
    public ResponseEntity<?> getAlarmsOfDate(@PathVariable("startDate")String startDate) {
        List<AlarmDto.AlarmDtoResponse> alarmList = alarmService.readAllAlarmOfDate(startDate);

        return new ResponseEntity<>(alarmList,HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiImplicitParam(name = "id",value = "해당아이디",required = true,paramType = "path")
    @ApiOperation(value = "해당구분아이디 알람 목록",notes = "성공시 사용자의 해당하는 구분아이디의 알람을 반환합니다.",response = AlarmDto.AlarmDtoResponse.class)
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getAlarmsOfId(@PathVariable("id")Long id) {
        AlarmDto.AlarmDtoResponse alarmList = alarmService.readAllAlarmOfId(id);

        return new ResponseEntity<>(alarmList,HttpStatus.OK);
    }
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiOperation(value = "알람 목록",notes = "성공시 사용자의 모든 알람을 반환합니다.",response = AlarmDto.AlarmDtoResponse.class)
    @GetMapping("")
    public ResponseEntity<?> getAlarms() {
        List<AlarmDto.AlarmDtoResponse> alarmList = alarmService.readAllAlarm();

        return new ResponseEntity<>(alarmList,HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 201, message = "작성됨")
    })
    @ApiOperation(value = "알람 저장",notes = "성공시 해당 유저의 알람을 저장합니다.")
    @PostMapping("")
    // 성공: 201 Created
    public ResponseEntity<?> postAlarms(@RequestBody AlarmDto.AlarmDtoRequest alarmDto){
        AlarmDto.AlarmDtoResponse savedAlarm = alarmService.saveAlarm(alarmDto);

        return new ResponseEntity<>(savedAlarm,HttpStatus.CREATED);
    }

    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    @ApiImplicitParam(name = "alarm_id",value = "알람번호",required = true,paramType = "path")
    @ApiOperation(value = "알람 수정",notes = "성공시 해당 알람의 내용을 변경합니다.")
    @PutMapping("/{alarm_id}")
    // 성공: 200 OK
    // 실패: 404 NOT FOUND
    public ResponseEntity<?> putAlarms(@PathVariable("alarm_id") Long alarmId, @RequestBody AlarmDto.AlarmDtoRequest alarmDto){
        AlarmDto.AlarmDtoResponse savedAlarm = alarmService.readAndUpdateAlarm(alarmId, alarmDto);

        if (savedAlarm == null) {
            return new ResponseEntity<>(alarmDto, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(savedAlarm, HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 204, message = "콘텐츠 없음")
    })
    @ApiImplicitParam(name = "alarm_id",value = "알람번호",required = true,paramType = "path")
    @ApiOperation(value = "알람 삭제",notes = "성공시 해당 알람을 삭제합니다.")
    @DeleteMapping("/{alarm_id}")
    public ResponseEntity<?> deleteAlarms(@PathVariable("alarm_id") Long alarmId){
        alarmService.removeAlarm(alarmId);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("alarm_id", alarmId);
        result.put("alarm", data);

        return new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
    }
}