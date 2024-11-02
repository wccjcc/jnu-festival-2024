package com.jnu.festival.domain.bookmark.controller;

import com.jnu.festival.domain.bookmark.service.PartnerBookmarkService;
import com.jnu.festival.domain.zone.DTO.response.ZoneListDto;
import com.jnu.festival.global.error.ErrorCode;
import com.jnu.festival.global.error.GlobalExceptionHandler;
import com.jnu.festival.global.error.exception.BusinessException;
import com.jnu.festival.global.security.UserDetailsImpl;
import com.jnu.festival.global.util.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class PartnerBookmarkControllerTest {

    @InjectMocks
    private PartnerBookmarkController partnerBookmarkController;

    @Mock
    private PartnerBookmarkService partnerBookmarkService;

    private MockMvc mockMvc;


    private WebApplicationContext wac;

    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(partnerBookmarkController).build();
    }

    @DisplayName("파트너 북마크 생성 성공")
    @WithMockUser
    @Test
    void createPartnerBookmark() throws Exception {
        Long partnerId = 1L;

        // given
        lenient().doNothing().when(partnerBookmarkService).createPartnerBookmark(anyLong(),any(UserDetailsImpl.class));

        // when & then
        mockMvc.perform(post("/api/v1/bookmarks/partners/{partnerId}", partnerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

    }

    @DisplayName("유효하지 않은 파트너 ID로 북마크 생성")
    @WithMockUser
    @Test
    void createPartnerBookmark_invalidPartnerId() throws Exception {
        Long invalidPartnerId = 999L;

        // given
        doThrow(new BusinessException(ErrorCode.NOT_FOUND_PARTNER)).when(partnerBookmarkService).createPartnerBookmark(eq(invalidPartnerId), any(UserDetailsImpl.class));


        // when & then
        mockMvc.perform(post("/api/v1/bookmarks/partners/{partnerId}", invalidPartnerId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.message").value("존재하지 않는 제휴업체입니다."));
    }



}
