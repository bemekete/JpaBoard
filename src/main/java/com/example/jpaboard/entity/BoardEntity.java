package com.example.jpaboard.entity;

import com.example.jpaboard.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// DB의 테이블 역할을 하는 클래스
// jpa에서는 필수적으로 사용해야하는 클래스
@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    // BaseEntity를 상속 받아 BoardEntity에서 시간정보 활용 가능
    @Id // Primary Key, 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 크기 20, not null
    private String boardWriter;

    @Column // default상태는 크기 255, null
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;
    
    // DTO에 담긴 값들을 Eneity객체로 옮겨담는 작업
    // 게시글 작성, 수정 한번에
    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();

        // boardDTO에 id가 없다면(게시글 작성)
        if (boardDTO.getId() == null) {
            boardEntity.setBoardHits(0);
        } else {
            // boardDTO에 id가 있다면(게시글 수정)
            boardEntity.setId(boardDTO.getId());
            boardEntity.setBoardHits(boardDTO.getBoardHits());
        }
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        return boardEntity;
    }

    // 게시글 수정(위 게시글 작성 코드에서 Id가 추가됨)
//    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
//        BoardEntity boardEntity = new BoardEntity();
//
//        boardEntity.setId(boardDTO.getId());
//        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
//        boardEntity.setBoardPass(boardDTO.getBoardPass());
//        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
//        boardEntity.setBoardContents(boardDTO.getBoardContents());
//        boardEntity.setBoardHits(boardDTO.getBoardHits());
//        return boardEntity;
//    }
}
