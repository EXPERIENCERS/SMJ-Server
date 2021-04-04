package com.experiencers.server.smj.controller;

import com.experiencers.server.smj.domain.Board;
import com.experiencers.server.smj.domain.Category;
import com.experiencers.server.smj.domain.Member;
import com.experiencers.server.smj.enumerate.BoardType;
import com.experiencers.server.smj.service.BoardService;
import com.experiencers.server.smj.service.CategoryService;
import com.experiencers.server.smj.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MemberService memberService;

    @GetMapping("/board")
    public ModelAndView getBoards(){
        ModelAndView response = new ModelAndView("board/index");

        List<Board> boardList = boardService.readAllBoard();
        response.addObject("boards", boardList);
        response.addObject("types", BoardType.values());

        List<Member> memberList = memberService.readAllMember();
        response.addObject("members", memberList);

        List<Category> categoryList = categoryService.readAllCategory();
        response.addObject("categories", categoryList);

        return response;
    }

    @PostMapping("/board")
    public String postBoard(@ModelAttribute Board inputtedBoard,@RequestParam(value = "category_id")Long categoryId,@RequestParam(value = "member_id") Long memberId){
        boardService.saveBoardOfAdmin(inputtedBoard,categoryId,memberId);

        return "redirect:/admin/board";
    }

    @GetMapping("/board/{board_id}/edit")
    public ModelAndView editBoard(@PathVariable("board_id") Long boardId){
        Board board = boardService.readBoard(boardId);

        ModelAndView response = new ModelAndView("board/edit");
        response.addObject("board", board);
        response.addObject("types", BoardType.values());

        List<Member> memberList = memberService.readAllMember();
        response.addObject("members", memberList);

        List<Category> categoryList = categoryService.readAllCategory();
        response.addObject("categories", categoryList);

        return response;
    }

    @PostMapping("/board/{board_id}/update")
    public String updateBoard(@ModelAttribute Board board,@RequestParam(value = "category_id")Long categoryId,@RequestParam(value = "board_id")Long boardId){
        boardService.readAndUpdateBoardOfAdmin(board, categoryId, boardId);

        return "redirect:/admin/board";
    }

    @PostMapping("/board/{board_id}/delete")
    public String deleteBoard(@PathVariable("board_id") Long boardId){
        boardService.deleteBoard(boardId);

        return "redirect:/admin/board";
    }


}