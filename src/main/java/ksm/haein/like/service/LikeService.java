package ksm.haein.like.service;

import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.like.entity.Like;
import ksm.haein.like.repository.LikeRepository;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void addLike(Long itemId, Long memberId) {
        Member memberProxy = memberRepository.getReferenceById(memberId);
        Item itemProxy = itemRepository.getReferenceById(itemId);

        Like like = Like.builder()
                .item(itemProxy)
                .member(memberProxy)
                .build();
        likeRepository.save(like);
    }
}
