package com.example.jpaboard.repository;

import com.example.jpaboard.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// JpaRepository<A, B> : A - Entity클래스 이름 , B - Entity클래스가 가지고 있는 pk의 타입
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    // 조회수 증가
    // update board_table set board_hits = board_hits + 1 where id = ? ->
    // 위와 같은 DB SQL문을 사용하고자 한다면 nativeQuery = true

    // update 엔티티이름 약어 set 엔티티필드명 where 엔티티필드명
    // :id는 아래 @Param("id")와 매칭
    // ex) :id123 -> @Param("id123")
    @Modifying // update, delete 쿼리를 실행할 때 필수
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id = :id")
    void updateHits(@Param("id") Long id);
}
