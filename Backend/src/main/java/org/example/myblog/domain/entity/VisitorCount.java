package org.example.myblog.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitorCount {
    private Long id;
    private Integer count;
    private LocalDateTime lastUpdateTime;
}
