package com.jnu.festival.domain.partner.Controller;

import com.jnu.festival.domain.partner.Service.PartnerService;
import com.jnu.festival.domain.partner.entity.Partner;
import com.jnu.festival.global.error.ErrorCode;
import com.jnu.festival.global.error.GlobalExceptionHandler;
import com.jnu.festival.global.error.exception.BusinessException;
import com.jnu.festival.global.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.mockito.BDDMockito.given;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
class PartnerControllerTest {

    @InjectMocks
    private PartnerController partnerController;

    @Mock
    private PartnerService partnerService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.standaloneSetup(partnerController).build();
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(partnerController)
                .setControllerAdvice(new GlobalExceptionHandler()) // 여기에 @ControllerAdvice 추가
                .build();
    }


    @DisplayName("제휴업체 상세조회 실패")
    @Test
    @WithMockUser(username = "testUser")
    @MockitoSettings(strictness = Strictness.LENIENT)
    void readPartnerByIdFailure() throws Exception {
        //given
        Partner partner1 = Partner.builder()
                .name("Test Partner")
                .location("Seoul")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .description("This is a test partner.")
                .build();
        Partner partner2 = Partner.builder()
                .name("Test Partner")
                .location("Seoul")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .description("This is a test partner.")
                .build();
        Long partnerId = 1000L;
//
////         UserDetailsImpl 객체 모킹
//        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
//        when(userDetails.getUsername()).thenReturn("testUser");

        // SecurityContext에 모킹된 UserDetailsImpl 설정
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        given(partnerService.readPartner(partnerId,null)).willThrow(new BusinessException(ErrorCode.NOT_FOUND_PARTNER));

        //when

        ResultActions result = mockMvc.perform(get("/api/v1/partners/{partnerId}", partnerId));
//

        //then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.message").value("존재하지 않는 제휴업체입니다."))
                .andDo(print());


    }
}