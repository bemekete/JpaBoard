package com.example.jpaboard.repository;

import com.example.jpaboard.entity.BoardEntity;
import com.example.jpaboard.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    List<CommentEntity> findAllByBoardEntityOrderByIdDesc(BoardEntity boardEntity);
    // select * from comment_table where board_id=? order by id desc;


}
