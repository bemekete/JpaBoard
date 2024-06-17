package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.dto.CommentDTO;
import com.example.jpaboard.service.BoardService;
import com.example.jpaboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    // 게시글 작성 폼으로 이동
    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    // 게시글 작성
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/paging";
    }

    // 게시글 전체 불러오기
    @GetMapping("/")
    public String findAll(Model model){
        // DB에서 전체 게시글 데이터를 가져와서 list.html에 뿌려준다
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList",boardDTOList);
        return "list";
    }

    // 게시글 상세보기
    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model, @PageableDefault(page = 1) Pageable pageable){
        // 해당 게시글의 조회수 업데이트
        boardService.updateHits(id);

        // 게시글 데이터 불러와서 detail.html에 출력
        BoardDTO boardDTO = boardService.findById(id);

        // 댓글목록 가져오기
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList",commentDTOList);

        model.addAttribute("board",boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        // @PageableDefault(page = 1) Pageable pageable 추가 이유는
        // 상세페이지에서 목록으로 돌아갈 때 일치하는 목록페이지로 가기 위함.
        // 54줄 코드로 detail 페이지로 갈 때 페이지정보를 가져간 후
        // 목록으로 갈때 다시 그 페이지정보를 가지고 일치하는 페이지로 이동
        return "detail";
    }

    // 게시글 수정 (수정할 정보 불러오기)
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate",boardDTO);
        return "update";
    }

    // 수정하고 수정한 내용 불러오기
    @PostMapping("update")
    public String update(@ModelAttribute BoardDTO boardDTO, Model model){
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board",board);
        return "detail";
    }

    // 게시글 삭제하기
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    // 페이징 기능
    // /board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 5; // 페이징버튼 개수 1~5, 6~10

        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1,4,7, ...
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        return "paging";
    }





























}
