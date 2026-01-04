package ksm.haein.item;

import jakarta.persistence.EntityNotFoundException;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.entity.Item;
import ksm.haein.item.repository.ItemRepository;
import ksm.haein.item.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스 테스트")
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    @DisplayName("상품 목록 조회 - 비회원")
    void testGetItemDataWithoutLike() {
        // Given
        int page = 0;
        List<ItemData> items = Arrays.asList(
                new ItemData(1L, "상품1", 10000, 8000, "빨강", "url1", false),
                new ItemData(2L, "상품2", 20000, 15000, "파랑", "url2", false)
        );
        Page<ItemData> pageData = new PageImpl<>(items);

        when(itemRepository.findItemByPageWithoutLike(any())).thenReturn(pageData);

        // When
        List<ItemData> result = itemService.getItemDataWithoutLike(page);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("상품1", result.get(0).name());
        verify(itemRepository).findItemByPageWithoutLike(any());
    }

    @Test
    @DisplayName("상품 목록 조회 - 회원")
    void testGetItemDataWithLike() {
        // Given
        int page = 0;
        long memberId = 1L;
        List<ItemData> items = Arrays.asList(
                new ItemData(1L, "상품1", 10000, 8000, "빨강", "url1", true),
                new ItemData(2L, "상품2", 20000, 15000, "파랑", "url2", false)
        );
        Page<ItemData> pageData = new PageImpl<>(items);

        when(itemRepository.findItemByPageWithLike(any(), eq(memberId))).thenReturn(pageData);

        // When
        List<ItemData> result = itemService.getItemDataWithLike(page, memberId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).like());
        assertFalse(result.get(1).like());
        verify(itemRepository).findItemByPageWithLike(any(), eq(memberId));
    }

    @Test
    @DisplayName("상품 상세 조회 - 성공")
    void testGetDetailItemDataSuccess() {
        // Given
        long itemId = 1L;

        // Mock Item 객체 생성 (Item은 builder가 없으므로 mock 사용)
        Item mockItem = mock(Item.class);
        when(mockItem.getId()).thenReturn(itemId);
        when(mockItem.getName()).thenReturn("상품 상세");
        when(mockItem.getPrice()).thenReturn(10000);
        when(mockItem.getSalePrice()).thenReturn(8000);
        when(mockItem.getShippingPrice()).thenReturn(2500);
        when(mockItem.getSize()).thenReturn("L");
        when(mockItem.getColor()).thenReturn("빨강");
        when(mockItem.getInformation()).thenReturn("상품 정보");
        when(mockItem.getShippingInfo()).thenReturn("결제 확인 후 1~2일 이내 발송됩니다.");
        when(mockItem.getPictures()).thenReturn(new ArrayList<>());
        when(mockItem.getReviews()).thenReturn(new ArrayList<>());
        when(mockItem.getQuestions()).thenReturn(new ArrayList<>());

        when(itemRepository.findItemWithDetailsById(itemId)).thenReturn(Optional.of(mockItem));

        // When
        DetailItemData result = itemService.getDetailItemData(itemId);

        // Then
        assertNotNull(result);
        assertEquals(itemId, result.getItemId());
        assertEquals("상품 상세", result.getName());
        verify(itemRepository).findItemWithDetailsById(itemId);
    }

    @Test
    @DisplayName("상품 상세 조회 - 실패 (존재하지 않는 상품)")
    void testGetDetailItemDataFailure() {
        // Given
        long itemId = 999L;

        when(itemRepository.findItemWithDetailsById(itemId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> itemService.getDetailItemData(itemId));
        verify(itemRepository).findItemWithDetailsById(itemId);
    }
}
