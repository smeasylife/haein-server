package ksm.haein.cart.service;

import ksm.haein.cart.entity.Cart;
import ksm.haein.cart.repository.CartRepository;
import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.user.entity.Member;
import ksm.haein.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void addCart(long memberId, long itemId) {
        Member memberProxy = memberRepository.getReferenceById(memberId);
        Item itemProxy = itemRepository.getReferenceById(itemId);

        Cart cart = Cart.builder()
                .member(memberProxy)
                .item(itemProxy)
                .build();

        cartRepository.save(cart);
    }
}
