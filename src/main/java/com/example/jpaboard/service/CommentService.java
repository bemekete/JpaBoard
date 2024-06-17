package com.example.jpaboard.service;

import com.example.jpaboard.dto.CommentDTO;
import com.example.jpaboard.entity.BoardEntity;
import com.example.jpaboard.entity.CommentEntity;
import com.example.jpaboard.repository.BoardRepository;
import com.example.jpaboard.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 댓글작성 기능
    public Long save(CommentDTO commentDTO) {
        // 부모엔티티(BoardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            System.out.println("서비스 24번째 줄 boardEntity에는 " + boardEntity + "가 담겨있다.");
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);

           return commentRepository.save(commentEntity).getId();
        }else{
            return null;
        }
    }

    public List<CommentDTO> findAll(Long boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        // select * from comment_table where board_id=? order by id desc;

        // EntityList -> DTOList
        List<CommentDTO> commentDTOList = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntityList){
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
