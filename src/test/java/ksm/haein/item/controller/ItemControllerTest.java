package ksm.haein.item.controller;

import ksm.haein.config.security.login.CustomUser;
import ksm.haein.item.dto.DetailItemData;
import ksm.haein.item.dto.ItemData;
import ksm.haein.item.service.ItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = ItemController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
@DisplayName("상품 컨트롤러 테스트")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    @WithAnonymousUser
    @DisplayName("상품 목록 조회 - 비회원")
    void testGetHomeItemsWithoutLike() throws Exception {
        // Given
        List<ItemData> mockItems = Arrays.asList(
                new ItemData(1L, "상품1", 10000, 8000, "빨강", "url1", false),
                new ItemData(2L, "상품2", 20000, 15000, "파랑", "url2", false)
        );
        when(itemService.getItemDataWithoutLike(0)).thenReturn(mockItems);

        // When & Then
        mockMvc.perform(get("/items")
                        .param("page", "0")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("상품1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("상품2"));

        verify(itemService).getItemDataWithoutLike(0);
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("상품 목록 조회 - 회원")
    void testGetHomeItemsWithLike() throws Exception {
        // Given
        Long userId = 1L;
        List<ItemData> mockItems = Arrays.asList(
                new ItemData(1L, "상품1", 10000, 8000, "빨강", "url1", true),
                new ItemData(2L, "상품2", 20000, 15000, "파랑", "url2", false)
        );
        when(itemService.getItemDataWithLike(0, userId)).thenReturn(mockItems);

        // When & Then
        mockMvc.perform(get("/items")
                        .param("page", "0")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].like").value(true))
                .andExpect(jsonPath("$[1].like").value(false));

        verify(itemService).getItemDataWithLike(0, userId);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("상품 상세 조회 - 성공")
    void testGetDetailItemDataSuccess() throws Exception {
        // Given
        Long itemId = 1L;
        DetailItemData mockDetailItem = new DetailItemData(
                itemId,
                "상품 상세",
                10000,
                8000,
                2500,
                "L",
                "빨강",
                "상품 정보",
                List.of(),
                List.of(),
                List.of()
        );
        when(itemService.getDetailItemData(itemId)).thenReturn(mockDetailItem);

        // When & Then
        mockMvc.perform(get("/items/{itemId}", itemId)
                        .param("itemId", String.valueOf(itemId))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId").value(itemId))
                .andExpect(jsonPath("$.name").value("상품 상세"))
                .andExpect(jsonPath("$.price").value(10000))
                .andExpect(jsonPath("$.salePrice").value(8000));

        verify(itemService).getDetailItemData(itemId);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("상품 목록 조회 - 페이지 파라미터 누락")
    void testGetHomeItemsMissingPageParam() throws Exception {
        // When & Then
        mockMvc.perform(get("/items")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
