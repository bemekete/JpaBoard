package com.example.jpaboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 시간정보를 다루는 테이블 따로 생성
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp // 생성시 시간 정보
    @Column(updatable = false) // update 관여 안함
    private LocalDateTime createdTime;

    @UpdateTimestamp // 업데이트 발생 시 시간정보
    @Column(insertable = false) // insert 관여 안함
    private LocalDateTime updatedTime;
}
