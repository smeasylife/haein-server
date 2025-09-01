package ksm.haein.review.dto;

import ksm.haein.review.entity.Review;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private String content;
    private LocalDateTime createdAt;

    public ReviewDto(Review review) {
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
    }
}
