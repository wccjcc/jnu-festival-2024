package com.jnu.festival.domain.bookmark.controller;

import com.jnu.festival.domain.bookmark.service.PartnerBookmarkService;
import com.jnu.festival.global.error.ErrorCode;
import com.jnu.festival.global.error.exception.BusinessException;
import com.jnu.festival.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
class PartnerBookmarkControllerTest {

    @InjectMocks
    private PartnerBookmarkController partnerBookmarkController;

    @Mock
    private PartnerBookmarkService partnerBookmarkService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(partnerBookmarkController).build();
    }

//    @DisplayName("제휴업체 즐겨찾기 생성 실패 - partnerId 찾을 수 없음")
//    @Test
//    void createPartnerBookmarkFailure_PartnerNotFound() throws Exception {
//        //given
//        Long partnerId = 1L;
//        //when
//        doThrow(new BusinessException(ErrorCode.NOT_FOUND_PARTNER))
//                .when(partnerBookmarkService).createPartnerBookmark(eq(partnerId), any(UserDetailsImpl.class));
//        //then
//        mockMvc.perform(post("/api/v1/bookmarks/partners/{partnerId}", partnerId))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("존재하지 않는 제휴업체입니다."))
//    }

    @DisplayName("제휴업체 즐겨찾기 생성 실패 - partnerId 찾을 수 없음")
    @Test
    void createPartnerBookmarkFailure_PartnerNotFound() throws Exception {
        // given
        Long partnerId = 1L;
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUsername()).willReturn("testUser");

        // when
        willThrow(new BusinessException(ErrorCode.NOT_FOUND_PARTNER))
                .given(partnerBookmarkService).createPartnerBookmark(eq(partnerId), any(UserDetailsImpl.class));

        // then (try catch로 해보기 내일)
        mockMvc.perform(post("/api/v1/bookmarks/partners/{partnerId}", partnerId)
                        .with(user(userDetails)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 제휴업체입니다."));
        //assertJ로 해보자 -> 아예 못잡아서그런고같다ㅏ
    }

    @DisplayName("제휴업체 즐겨찾기 생성 실패 - 사용자 인증 실패")
    @Test
    void createPartnerBookmarkFailure_UserNotFound() throws Exception {
        //given
        Long partnerId = 1L;
        //when
        doThrow(new BusinessException(ErrorCode.NOT_FOUND_USER))
                .when(partnerBookmarkService).createPartnerBookmark(eq(partnerId), any(UserDetailsImpl.class));
        //then
        mockMvc.perform(post("/api/v1/bookmarks/partners/{partnerId}", partnerId)
                        .with(user("testUser")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
    }

    @DisplayName("제휴업체 즐겨찾기 삭제 실패 - partnerId찾을 수 없음")
    @Test
    void deletePartnerBookmark_Failure_PartnerNotFound() throws Exception {
        //given
        Long partnerId = 1L;
        //when
        doThrow(new BusinessException(ErrorCode.NOT_FOUND_PARTNER))
                .when(partnerBookmarkService).deletePartnerBookmark(eq(partnerId), any(UserDetailsImpl.class));
        //then
        mockMvc.perform(delete("/api/v1/bookmarks/partners/{partnerId}", partnerId)
                        .with(user("testUser")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 제휴업체입니다."));
    }

    @DisplayName("제휴업체 즐겨찾기 삭제 실패 - 사용자 인증 실패")
    @Test
    void deletePartnerBookmarkFailure_UserNotFound() throws Exception {
        //given
        Long partnerId = 1L;
        //when
        doThrow(new BusinessException(ErrorCode.NOT_FOUND_USER))
                .when(partnerBookmarkService).deletePartnerBookmark(eq(partnerId), any(UserDetailsImpl.class));
        //then
        mockMvc.perform(delete("/api/v1/bookmarks/partners/{partnerId}", partnerId)
                        .with(user("testUser")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("존재하지 않는 사용자입니다."));
    }
}
