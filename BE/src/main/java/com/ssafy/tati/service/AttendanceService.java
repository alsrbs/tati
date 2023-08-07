package com.ssafy.tati.service;

import com.ssafy.tati.entity.Attendance;
import com.ssafy.tati.entity.Member;
import com.ssafy.tati.entity.StudyMember;
import com.ssafy.tati.entity.StudySchedule;
import com.ssafy.tati.repository.AttendanceRepository;
import com.ssafy.tati.repository.MemberRepository;
import com.ssafy.tati.repository.StudyMemberRepository;
import com.ssafy.tati.repository.StudyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyScheduleRepository studyScheduleRepository;
    
    // 입실
    public Attendance addAttendance(Attendance attendance) {
        Attendance createdAttendance = attendanceRepository.save(attendance);
        return createdAttendance;
    }

    // 퇴실
    public Attendance modifyAttendance(Attendance attendance, Integer memberId) {
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendance.getAttendanceId());
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalAttendance.isEmpty() || optionalMember.isEmpty()) {
            throw new RuntimeException();
        }
        Attendance modifyAttendance = optionalAttendance.get();
        Member modifyMember = optionalMember.get();
        if (!modifyAttendance.getMember().equals(modifyMember)) {
            throw new RuntimeException();
        }

        modifyAttendance.setOutTime(attendance.getOutTime());
        StudyMember modifyStudyMember = modifyAttendance.getStudyMember();
        Integer studyId = modifyStudyMember.getStudy().getStudyId();

        Optional<StudySchedule> optionalStudySchedule = studyScheduleRepository.findByStudyIdAndStudyDay(studyId, "");
        if (optionalStudySchedule.isEmpty()) {
            throw new RuntimeException();
        }
        StudySchedule studySchedule = optionalStudySchedule.get();
//        studySchedule.getStudyStartTime();
        ////////////////////////////////////
        // 요일, 시작시간, 종료시간 타입 비교

        // todo 스터디 일정 가져와서 출석 여부 나누기

//        if (결석) {
//            // 출석 여부
//            modifyAttendance.setIsAttended('0');
//            // 상벌점
//            modifyAttendance.setScore(-4);
//            // 멤버 누적 상벌점
//            Integer beforeScore = modifyMember.getTotalScore();
//            modifyMember.setTotalScore(beforeScore - 4);
//            Integer beforeAbsenceCount = modifyStudyMember.getAbsenceCount();
//            modifyStudyMember.setAbsenceCount(beforeAbsenceCount + 2);
//            // 벌금
//            Integer pelaltyAmt = modifyStudyMember.getStudy().getStudyDeposit() / 3;
//            modifyAttendance.setPenaltyAmt(pelaltyAmt.shortValue());
//            modifyStudyMember.setTotalPenalty(modifyMember.getTotalPoint() + pelaltyAmt);
//        } else if (지각){
//            // 출석 여부
//            modifyAttendance.setIsAttended('1');
//            // 상벌점
//            modifyAttendance.setScore(-2);
//            // 멤버 누적 상벌점
//            Integer beforeScore = modifyMember.getTotalScore();
//            modifyMember.setTotalScore(beforeScore - 2);
//            Integer beforeAbsenceCount = modifyStudyMember.getAbsenceCount();
//            modifyStudyMember.setAbsenceCount(beforeAbsenceCount + 1);
//
//            if (modifyStudyMember.getAbsenceCount() % 2 == 0) {
//                Integer pelaltyAmt = modifyStudyMember.getStudy().getStudyDeposit() / 3;
//                modifyAttendance.setPenaltyAmt(pelaltyAmt.shortValue());
//                modifyStudyMember.setTotalPenalty(modifyMember.getTotalPoint() + pelaltyAmt);
//            }
//        } else { // 출석
//            // 출석 여부
//            modifyAttendance.setIsAttended('2');
//            // 상벌점
//            modifyAttendance.setScore(1);
//            // 멤버 누적 상벌점 (열정지수)
//            Integer beforeScore = modifyMember.getTotalScore();
//            modifyMember.setTotalScore(beforeScore + 1);
//            Integer beforeAbsenceCount = modifyStudyMember.getAbsenceCount();
//            modifyStudyMember.setAbsenceCount(beforeAbsenceCount + 1);
//        }
//
//        // AbsenceCount가 6이상이면 진행 스터디회원에서 삭제
//        if (modifyStudyMember.getAbsenceCount() >= 6) {
//            studyMemberRepository.delete(modifyStudyMember);
//        }
        return modifyAttendance;
    }
}
