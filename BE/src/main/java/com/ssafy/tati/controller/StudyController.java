package com.ssafy.tati.controller;

import com.ssafy.tati.dto.req.StudyReqDto;
import com.ssafy.tati.dto.req.StudyScheduleReqDto;
import com.ssafy.tati.entity.Study;
import com.ssafy.tati.entity.StudySchedule;
import com.ssafy.tati.mapper.StudyMapper;
import com.ssafy.tati.service.StudyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "스터디", description = "스터디 API 문서")
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final StudyMapper studyMapper;

    @PostMapping("/create")
    public ResponseEntity<?> createStudy(@RequestBody StudyReqDto studyReqDto) {
        Study study = studyMapper.studyReqDtoToStudy(studyReqDto);
        studyService.createStudy(study);
        List<StudyScheduleReqDto> studyScheduleReqDtoList = studyReqDto.getStudySchedule();
        List<StudySchedule> studyScheduleList = studyMapper.studyReqScheduleListToStudySchedule(studyScheduleReqDtoList);
        for (StudySchedule studySchedule: studyScheduleList) {
            studySchedule.setStudy(study);
            studyService.createStudySchedule(studySchedule);
        }
        return new ResponseEntity<>(study, HttpStatus.CREATED);
    }

//    @GetMapping()

}
