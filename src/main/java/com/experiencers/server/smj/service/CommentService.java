package com.experiencers.server.smj.service;

import com.experiencers.server.smj.domain.Board;
import com.experiencers.server.smj.domain.Member;
import com.experiencers.server.smj.dto.CommentDto;
import com.experiencers.server.smj.manager.MemberManager;
import com.experiencers.server.smj.repository.BoardRepository;
import com.experiencers.server.smj.domain.Comment;
import com.experiencers.server.smj.repository.CommentRepository;
import com.experiencers.server.smj.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberManager memberManager;

    public CommentDto.CommentDtoResponse saveComment(CommentDto.CommentDtoRequest commentDto, Long boardId){
        Board board = boardRepository.findById(boardId).get();
        Member member = memberManager.getMember();

        Comment comment = commentDto.toEntity(board,member);
        Comment saveComment = commentRepository.save(comment);

        return CommentDto.CommentDtoResponse.of(saveComment);
    }
    public CommentDto.CommentDtoResponse readAndUpdateComment(Long commentId, CommentDto.CommentDtoRequest commentDto){
        Optional<Comment> data = commentRepository.findById(commentId);

        if(data.isPresent()){
            Comment target = data.get();
            target.setContent(commentDto.getContent());
            target = commentRepository.save(target);

            return CommentDto.CommentDtoResponse.of(target);
        }
        return null;
    }
    public List<CommentDto.CommentDtoResponse> readComment(Long boardId){
        List<Comment> commentList = boardRepository.findById(boardId).get().getComments();

        return CommentDto.CommentDtoResponse.of(commentList);
    }


    //Api Admin Service
    public List<Comment> readAllComment(){
        return commentRepository.findAll();
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

    //Admin Service
    public Comment readCommentOfAdmin(Long commentId){
        return commentRepository.findById(commentId).get();
    }

    public Comment saveCommentOfAdmin(Comment inputtedComment, Long boardId, Long memberId){
        Board board = boardRepository.findById(boardId).get();

        inputtedComment.setBoard(board);

        Member member = memberRepository.findById(memberId).get();

        inputtedComment.setMember(member);

        Comment savedComment = commentRepository.save(inputtedComment);

        return savedComment;

    }

    public Comment readAndUpdateCommentOfAdmin(Long commentId, Comment comment){
        Optional<Comment> data = commentRepository.findById(commentId);

        if(data.isPresent()){
            Comment target = data.get();

            //target.setUser(comment.getUser());
            target.setContent(comment.getContent());


            target = commentRepository.save(target);

            return target;
        }
        return null;
    }

}