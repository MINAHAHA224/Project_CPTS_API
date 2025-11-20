package vn.javaweb.ComputerShop.domain.entity.abstracts;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class Auditable extends BaseIdEntity<Long> {

    @Column(insertable = false , updatable = false)
    private LocalDateTime createdAt;

    @Column(insertable = false)
    private LocalDateTime updatedAt;


}