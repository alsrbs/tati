package com.ssafy.tati.mapper;

import com.ssafy.tati.dto.req.PostCommentReqDto;
import com.ssafy.tati.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    default Comment commentReqDtoToComment(PostCommentReqDto postCommentReqDto) {
        if(postCommentReqDto == null) return null;

        Comment comment = new Comment();
        comment.setCommentContent(postCommentReqDto.getCommentContent());

        return comment;
    }
}
