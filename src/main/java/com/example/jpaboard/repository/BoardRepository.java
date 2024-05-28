package com.example.jpaboard.repository;

import com.example.jpaboard.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<A, B> : A - entity클래스 이름 , B - entity클래스가 가지고 있는 pk의 타입
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    
}
