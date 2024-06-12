package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.entity.BoardEntity;
import com.example.jpaboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        for (BoardEntity boardEntity : boardEntityList) {
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
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    // 게시글 수정하기
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    // 게시글 삭제하기
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    // 페이징 기능
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1;
        // 86번줄 jpa에서 sql 쿼리 limit 사용 (limit ?,?) -> 인덱스값은 0부터 시작 -> -1해줘야함
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수

        // 한 페이지당 3개씩 글을 보여주고 정렬 기준은  id 기준으로 내림차순 정렬
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));
        // page 위치에 있는 값은 0부터 시작

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // boardEntities에서 하나씩 거내서 BoardDTO로 옮겨준다
        // 목록 : 게시글번호, 작성자, 제목, 조회수, 생성시간
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO
                (board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));

        return boardDTOS;
    }


}
