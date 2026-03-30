package com.example.BE.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Setter
//Đánh dấu class này không phải là entity riêng biệt mà
// là class cha, các class con kế thừa sẽ có các field này ánh xạ xuống database
@MappedSuperclass
//Cho phép tự động điền giá trị cho các trường auditing như createdBy, createdAt, updatedBy, updatedAt
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate  // Tự động set khi entity được persist lần đầu
    @CreationTimestamp// Hibernate annotation, tương tự @CreatedDate
    @Column(name = "CREATED_AT", nullable = false,updatable = false) //updatable = false
    private Instant createdAt;

    @CreatedBy // Tự động set người tạo
    @Column(name = "CREATED_BY", nullable = false, length = 20, updatable = false)
    private String createdBy;

    @LastModifiedDate // Tự động update mỗi khi entity thay đổi
    @UpdateTimestamp
    @Column(name = "UPDATED_AT",insertable = false)
    private Instant updatedAt;

    @LastModifiedBy // Tự động set người sửa cuối cùng
    @Column(name = "UPDATED_BY", length = 20,insertable = false)
    private String updatedBy;//Lưu người thực hiện cập nhật cuối cùng

}
