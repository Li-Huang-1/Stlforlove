package org.example.myblog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitorCountVO {
    private Integer count;
    private LocalDateTime lastUpdateTime;
}
