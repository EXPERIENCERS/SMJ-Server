package com.experiencers.server.smj.service;

import com.experiencers.server.smj.domain.Board;
import com.experiencers.server.smj.domain.Category;
import com.experiencers.server.smj.domain.Member;
import com.experiencers.server.smj.dto.BoardDto;
import com.experiencers.server.smj.enumerate.BoardType;
import com.experiencers.server.smj.manager.MemberManager;
import com.experiencers.server.smj.repository.BoardRepository;
import com.experiencers.server.smj.repository.CategoryRepository;
import com.experiencers.server.smj.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberManager memberManager;
    @Autowired
    private CategoryRepository categoryRepository;

    //Api service
    public BoardDto.BoardDtoResponse saveBoard(BoardDto.BoardDtoRequest boardDto){
        Member member = memberManager.getMember();

        if(boardDto.getType().toString().equals("TRADE")){
            boardDto.setType(BoardType.TRADE);
        }else {
            boardDto.setType(BoardType.LIVE);
        }

        Optional<Category> categoryOptional = categoryRepository.findById(boardDto.getCategoryId());
        Category category = null;
        if(!categoryOptional.isPresent()){
            category = new Category();
        }else{
            category = categoryOptional.get();
        }

        Board board = boardDto.toEntity(category, member);
        Board saveBoard = boardRepository.save(board);

        return BoardDto.BoardDtoResponse.of(saveBoard);
    }
    public BoardDto.BoardDtoResponse readAndUpdateBoard(Long boardId, BoardDto.BoardDtoRequest boardDto){

        Optional<Board> data = boardRepository.findById(boardId);
        if(data.isPresent()){
            Board target = data.get();
            target.setTitle(boardDto.getTitle());
            target.setContent(boardDto.getContent());
            target.setImageOne(boardDto.getImageOne());
            target.setImageTwo(boardDto.getImageTwo());
            target.setImageThree(boardDto.getImageThree());

            if(boardDto.getType().toString().equals("TRADE")){
                target.setType(BoardType.TRADE);
            }else {
                target.setType(BoardType.LIVE);
            }

            Optional<Category> categoryOptional = categoryRepository.findById(boardDto.getCategoryId());
            if(!categoryOptional.isPresent()){
                target.setCategory(new Category());
            }else{
                target.setCategory(categoryOptional.get());
            }

            target = boardRepository.save(target);

            return BoardDto.BoardDtoResponse.of(target);
        }

        return null;
    }

    public List<BoardDto.BoardDtoResponse> readMyBoard(){
        Member member = memberManager.getMember();
        List<Board> boards = boardRepository.findAllByMember_EmailEquals(member.getEmail());

        return BoardDto.BoardDtoResponse.of(boards);
    }

    public List<BoardDto.BoardDtoResponse> readAllBoardofApi() {
        List<Board> boardList = boardRepository.findAll();

        return BoardDto.BoardDtoResponse.of(boardList);
    }

    //delete - Api, Admin 공통
    public void deleteBoard(Long boardId) {
        boardRepository.deleteById(boardId);
    }

    //Admin service
    public List<Board> readAllBoard(){
            return boardRepository.findAll();
    }

    public Board readBoard(Long boardId){return boardRepository.findById(boardId).get();}

    public Board saveBoardOfAdmin(Board inputtedBoard,Long categoryId,Long memberId){

        Member member = memberRepository.findById(memberId).get();
        inputtedBoard.setMember(member);

        Category category = categoryRepository.findById(categoryId).get();
        inputtedBoard.setCategory(category);

        Board savedBoard = boardRepository.save(inputtedBoard);

        return savedBoard;
    }

    public Board readAndUpdateBoardOfAdmin(Board board,Long categoryId,Long boardId){
        Optional<Board> data = boardRepository.findById(boardId);
        if(data.isPresent()) {
            Board target = data.get();
            target.setTitle(board.getTitle());
            target.setContent(board.getContent());

            if(board.getType().toString().equals("TRADE")){
                target.setType(BoardType.TRADE);
            }else {
                target.setType(BoardType.LIVE);
            }

            Category category = categoryRepository.findById(categoryId).get();
            target.setCategory(category);

            target = boardRepository.save(target);

            return target;
        }
        return null;
    }
}
