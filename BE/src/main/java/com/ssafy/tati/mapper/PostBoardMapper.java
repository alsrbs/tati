package com.ssafy.tati.mapper;

import com.ssafy.tati.dto.req.PostBoardReqDto;
import com.ssafy.tati.dto.req.PostStudyBoardReqDto;
import com.ssafy.tati.entity.Board;

public interface PostBoardMapper {
    default Board postBoardReqDtoToBoard(char boardType, PostBoardReqDto postBoardReqDto) {
        if (postBoardReqDto == null) return null;

        Board board = new Board();
        board.setBoardType(boardType);
        board.setBoardTitle(postBoardReqDto.getBoardTitle());
        board.setBoardContent(postBoardReqDto.getBoardContent());
        board.setBoardHit(0);

        return board;
    }
    default Board postStudyBoardReqDtoToBoard(char boardType, PostStudyBoardReqDto postStudyBoardReqDto) {
        if (postStudyBoardReqDto == null) return null;

        Board board = new Board();
        board.setBoardType(boardType);
        board.setBoardTitle(postStudyBoardReqDto.getBoardTitle());
        board.setBoardContent(postStudyBoardReqDto.getBoardContent());
        board.setBoardHit(0);

        return board;
    }
}
