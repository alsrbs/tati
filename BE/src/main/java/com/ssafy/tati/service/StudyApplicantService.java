package com.ssafy.tati.service;

import com.ssafy.tati.dto.res.StudyApplicantApprovalMemberResDto;
import com.ssafy.tati.dto.res.StudyIdResDto;
import com.ssafy.tati.entity.Member;
import com.ssafy.tati.entity.Study;
import com.ssafy.tati.entity.StudyApplicant;
import com.ssafy.tati.entity.StudyMember;
import com.ssafy.tati.exception.DataNotFoundException;
import com.ssafy.tati.exception.PointException;
import com.ssafy.tati.repository.*;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudyApplicantService {
    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyApplicantRepository studyApplicantRepository;
    private final StudyMemberRepository studyMemberRepository;

    public StudyIdResDto studyApplicantMember(Integer studyId, Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new DataNotFoundException("등록된 회원이 아닙니다."));
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new DataNotFoundException("스터디가 존재하지 않습니다."));

        Integer point = member.getTotalPoint() - study.getStudyDeposit();
        if (point < 0) { new PointException("스터디에 참가하는데 포인트가 부족합니다");  }

        StudyApplicant studyApplicant = null;
        member.updateTotalPoint(point);
        Optional<StudyApplicant> optionalStudyApplicant = studyApplicantRepository.findByMemberMemberIdAndStudyStudyId(memberId, studyId);
        if(optionalStudyApplicant.isPresent()) {
            throw new DuplicateRequestException("이미 신청한 스터디입니다.");
        }

        studyApplicant = new StudyApplicant(0, study, member);

        studyApplicantRepository.save(studyApplicant);
        StudyIdResDto studyIdResDto = new StudyIdResDto(memberId);
        return studyIdResDto;
    }

    @Transactional(readOnly = true)
    public List<Member> getStudyApplicantMember(Integer studyId) {
//        Study study = studyRepository.findById(studyId).orElseThrow(() -> new RuntimeException("study가 존재하지 않습니다."));
//        List<StudyApplicant> studyApplicantList = studyApplicantRepository.findAllByStudyStudyId(studyId);
//        System.out.println("서비스 : " + studyApplicantList);
//        Optional<StudyApplicant> optionalStudyApplicant = studyApplicantRepository.findByStudyStudyId(studyId);
//        System.out.println("서비스 : " + optionalStudyApplicant.get());
//        List<StudyApplicantMemberResDto> studyApplicantMemberResDtoList = null;
//        for (StudyApplicant studyApplicant: studyApplicantList) {
//            Member member = studyApplicant.getMember();
//            StudyApplicantMemberResDto studyApplicantMemberResDto = new StudyApplicantMemberResDto(member.getMemberId(), member.getMemberNickName(), member.getTotalScore(), member.getTotalStudyTime());
//            studyApplicantMemberResDtoList.add(studyApplicantMemberResDto);
//        }
        List<StudyApplicant> studyApplicants = studyApplicantRepository.findAllByStudyStudyId(studyId);
        List<Integer> memberIds = studyApplicants.stream().map(StudyApplicant::getMemberId).collect(Collectors.toList());
        return memberRepository.findByMemberIdIn(memberIds);
    }

//    public StudyApplicantApprovalMemberResDto getStudyApplicantApprovalMember(Integer studyId, Integer memberId){
//        StudyApplicant studyApplicant = studyApplicantRepository.findByMemberMemberIdAndStudyStudyId(memberId, studyId).orElseThrow(() -> new RuntimeException("해당 스터디 신청 회원이 아닙니다."));
//
//        LocalDate localDate = LocalDate.now();
//        studyMemberRepository.save(new StudyMember(studyApplicant.getStudyApplicantDeposit(), localDate, studyApplicant.getStudy(), studyApplicant.getMember()));
//        StudyApplicantApprovalMemberResDto studyApplicantApprovalMemberResDto = new StudyApplicantApprovalMemberResDto(memberId, studyId, studyApplicant.getMember().getMemberNickName());
//        studyApplicantRepository.deleteById(studyApplicant.getStudyApplicantId());
//        return studyApplicantApprovalMemberResDto;
//    }
//
//    public StudyApplicantApprovalMemberResDto getStudyApplicantRefuseMember(Integer studyId, Integer memberId) {
//        StudyApplicant studyApplicant = studyApplicantRepository.findByMemberMemberIdAndStudyStudyId(memberId, studyId).orElseThrow(() -> new RuntimeException("해당 스터디 신청 회원이 아닙니다."));
//        Member member = studyApplicant.getMember();
//        member.setTotalPoint(member.getTotalPoint() + studyApplicant.getStudyApplicantDeposit());
//        StudyApplicantApprovalMemberResDto studyApplicantApprovalMemberResDto = new StudyApplicantApprovalMemberResDto(memberId, studyId, studyApplicant.getMember().getMemberNickName());
//        return studyApplicantApprovalMemberResDto;
//    }
}
