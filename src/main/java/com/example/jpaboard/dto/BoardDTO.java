package com.example.jpaboard.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자
public class BoardDTO {
    private Long id; // 글번호
    private String boardWriter; // 작성자
    private String boardPass; // 비밀번호
    private String boardTitle; // 제목
    private String boardContents; // 내용
    private int boardHits; // 조회수
    private LocalDateTime boardCreatedTime; // 게시글 작성시간
    private LocalDateTime boardUpdatedTime; // 게시글 수정시간
}
