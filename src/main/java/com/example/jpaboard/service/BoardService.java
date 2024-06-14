package com.example.jpaboard.service;

import com.example.jpaboard.dto.BoardDTO;
import com.example.jpaboard.entity.BoardEntity;
import com.example.jpaboard.entity.BoardFileEntity;
import com.example.jpaboard.repository.BoardFileRepository;
import com.example.jpaboard.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity class 에서 작업)
// Entity ->DTO (DTO class 에서 작업)

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // 게시글 작성(저장)
    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
//        if (boardDTO.getFileAttached() == 1) {
        if (boardDTO.getBoardFile().isEmpty()) {
            // 첨부파일 없음
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);

        } else {
            // 첨부파일 있음
            /*
                1. DTO에 담긴 파일을 꺼냄
                2. 파일의 이름 가져옴
                3. 서버 저장용 이름은 만듦
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table에 해당 데이터 save 처리
                7. board_file_table에 해당 데이터 save 처리
            */

            // 다중파일 불러올 때
            // 1. 첨부파일이 있다면 DTO를 Entity로 변환해서 DB에 저장 후 부모엔티티의 아이디값을 가져온다.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long saveId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(saveId).get();

            // 2. 반복문으로 첨부 파일을 하나씩 불러와서 로컬경로에 저장한 후
            // 3. 부모엔티티를 참조하고있는 자식엔티티에 정보를 넣어 DB에 저장
            for (MultipartFile boardFile : boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/ProjectHwang/uploadFiles/" + storedFileName;
                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }

            // 단일파일 불러올 때
//            // 1~5
//            MultipartFile boardFile = boardDTO.getBoardFile();
//            String originalFilename = boardFile.getOriginalFilename();
//            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
//            String savePath = "C:/ProjectHwang/uploadFiles/" + storedFileName;
//            boardFile.transferTo(new File(savePath));
//
//            // 6
//            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
//            Long saveId = boardRepository.save(boardEntity).getId();
//            BoardEntity board = boardRepository.findById(saveId).get();
//
//            // 7
//            BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
//            boardFileRepository.save(boardFileEntity);
        }
    }

    // 게시글목록 전체 불러오기
    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<BoardDTO>();
        // entity 객체를 DTO에 옮겨 담아야 하기 때문에 BoardDTO 클래스에 코드 생성

        // boardEntityList에서 하나씩 꺼내서
        // toBoardDTO 메서드로 DTO에 옮겨담아
        // boardDTOList에 담는다
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity)); // List용 DTO 따로 만들어보기
        }

        return boardDTOList;
    }

    // 조회수 증가
    @Transactional // jpa에서 제공하는 메서드가 아닌 별도로 추가된 메서드를 사용할 때 사용
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }


    // 게시글 상세보기
    @Transactional
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
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
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
