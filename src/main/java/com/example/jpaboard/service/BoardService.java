package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.entity.BoardEntity;
import com.example.jpaboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity class 에서 작업)
// Entity ->DTO (DTO class 에서 작업)

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 작성(저장)
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }

    // 게시글 전체 불러오기
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<BoardDTO>();
        // entity 객체를 DTO에 옮겨 담아야 하기 때문에 BoardDTO 클래스에 코드 생성

        // boardEntityList에서 하나씩 꺼내서
        // toBoardDTO 메서드로 DTO에 옮겨담아
        // boardDTOList에 담는다
        for (BoardEntity boardEntity : boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }

        return boardDTOList;
    }
    
    // 조회수 증가
    @Transactional // jpa에서 제공하는 메서드가 아닌 별도로 추가된 메서드를 사용할 때 사용
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    // 게시글 상세보기
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()){
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        }else{
            return null;
        }
    }
}
