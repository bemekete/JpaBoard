package com.example.jpaboard.controller;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    // 게시글 작성 폼으로 이동
    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }

    // 게시글 작성
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO){
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/";
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
    public String findById(@PathVariable("id") Long id, Model model){
        // 해당 게시글의 조회수 업데이트
        boardService.updateHits(id);

        // 게시글 데이터 불러와서 detail.html에 출력
        BoardDTO boardDTO = boardService.findById(id);

        model.addAttribute("board",boardDTO);
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
}
