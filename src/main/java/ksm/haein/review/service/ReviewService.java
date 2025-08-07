package ksm.haein.review.service;

import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.review.entity.Review;
import ksm.haein.review.repository.ReviewRepository;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void addReview(String content, long itemId, long memberId) {
        Member memberProxy = memberRepository.getReferenceById(memberId);
        Item itemProxy = itemRepository.getReferenceById(itemId);

        Review review = Review.builder()
                .content(content)
                .member(memberProxy)
                .item(itemProxy)
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);
    }
}
